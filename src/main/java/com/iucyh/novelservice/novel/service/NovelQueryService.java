package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.common.exception.DataNotFound;
import com.iucyh.novelservice.common.response.PageWithCursorResponse;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.repository.custom.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.custom.paging.NovelPagingStrategy;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.service.codec.NovelCursorCodec;
import com.iucyh.novelservice.novel.service.dto.query.GetNewNovelsQuery;
import com.iucyh.novelservice.novel.service.dto.query.GetNovelsQuery;
import com.iucyh.novelservice.novel.service.policy.NovelPolicyValidator;
import com.iucyh.novelservice.novel.service.registry.NovelPagingStrategyRegistry;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.response.NovelDetailResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NovelQueryService {

    private final NovelCursorCodec cursorCodec;
    private final NovelPagingStrategyRegistry pagingStrategyRegistry;
    private final NovelRepository novelRepository;

    private final NovelPolicyValidator novelPolicyValidator;

    public PageWithCursorResponse<NovelSummaryResponse> getNovels(GetNovelsQuery query) {
        return findNovels(query.sortType(), query.cursor(), query.limit(),
                (pagingCondition, strategy) ->
                        novelRepository.findNovels(pagingCondition, strategy, query.category())
        );
    }

    public PageWithCursorResponse<NovelSummaryResponse> getNewNovels(GetNewNovelsQuery query) {
        return findNovels(query.sortType(), query.cursor(), query.limit(),
                (pagingCondition, strategy) ->
                        novelRepository.findNewNovels(pagingCondition, strategy, query.category())
        );
    }

    public NovelDetailResponse getNovelDetail(String novelPublicId) {
        Novel novel = novelRepository.findByPublicIdFetch(novelPublicId)
                .orElseThrow(() -> new DataNotFound(novelPublicId));
        return NovelResponseMapper.toNovelDetailResponse(novel);
    }

    /**
     * <p>Novel Page 조회를 위한 공통 메서드</p>
     * @param finder 각 조회 종류별로 필요한 메서드 호출 로직을 담는 람다식
     *               <br>
     *               e.g) 조회 종류별 리포지토리 조회 메서드 호출 등
     * @return 최종 결과를 담은 {@code PageWithCursorResponse<NovelSummaryResponse>}
     */
    private PageWithCursorResponse<NovelSummaryResponse> findNovels(
            NovelSortType sortType, String cursor, int limit,
            BiFunction<NovelPagingCondition, NovelPagingStrategy, List<Novel>> finder
    ) {
        NovelPagingStrategy pagingStrategy = pagingStrategyRegistry.get(sortType);
        NovelCursor decodedCursor = decodeCursor(cursor, pagingStrategy.getCursorClass());

        // JSON은 필드 순서를 신경쓰지 않기 때문에 필드명만 같다면 서로 다른 정렬 기준끼리도 커서가 호환될 수 있으므로 별도로 추가 검증
        if (decodedCursor != null) {
            novelPolicyValidator.validateNovelCursorMatchesSortType(decodedCursor, sortType);
        }

        // 다음 페이지가 있는지 확인하기 위해 limit + 1개 만큼 가져오기(결과의 size가 limit + 1 이라면 다음 페이지 존재)
        NovelPagingCondition pagingCondition = new NovelPagingCondition(decodedCursor, limit + 1);
        List<Novel> result = finder.apply(pagingCondition, pagingStrategy);

        if (result.isEmpty()) {
            return NovelResponseMapper.toPageResponse(List.of(), null, limit);
        }

        List<Novel> limitedResult = result.stream().limit(limit).toList(); // 클라이언트로 응답할 최종 결과 (limit만큼 자르기)
        PageSizeInfo pageSizeInfo = new PageSizeInfo(result.size(), limit);
        String newCursor = createNewCursor(pagingStrategy, limitedResult, pageSizeInfo);

        return NovelResponseMapper.toPageResponse(limitedResult, newCursor, limit);
    }

    /**
     * <p>클라이언트로부터 받은 {@code String} 타입의 커서를 {@code NovelCursor} 타입의 객체로 디코딩</p>
     * @param cursor 클라이언트로부터 받은 원본 커서
     * @param cursorClass 디코딩 될 {@code NovelCursor} 클래스 (앞서 선택된 전략의 getCursorClass() 메서드 사용 권장)
     * @return {@code cursor}가 {@code null}이거나 비어있다면(blank) {@code null}, 아니라면 디코딩 된 {@code NovelCursor} 객체
     */
    private NovelCursor decodeCursor(String cursor, Class<? extends NovelCursor> cursorClass) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }

        return cursorCodec.decode(cursor, cursorClass);
    }

    /**
     * <p>현재 페이지 결과에 대한 인코딩 된 새 커서 생성</p>
     * @param pagingStrategy 조회 시 사용한 전략
     * @param pageResult 원본이 아닌 외부(클라이언트)로 내보낼 가공이 끝난 최종 결과 리스트
     * @param sizeInfo 페이지 결과의 크기 관련 정보 (자세한 내용은 {@code PageSizeInfo} 문서 주석 참고, 필독 요망)
     * @return 커서 생성 조건에 부합하지 않다면 {@code null}, 부합하다면 인코딩된 {@code String} 타입의 새 커서
     */
    private String createNewCursor(NovelPagingStrategy pagingStrategy, List<Novel> pageResult, PageSizeInfo sizeInfo) {
        if (!sizeInfo.hasNextPage()) {
            return null;
        }

        Novel lastResult = pageResult.get(pageResult.size() - 1);
        NovelCursor newCursor = pagingStrategy.createCursor(lastResult);
        return cursorCodec.encode(newCursor);
    }

    /**
     * <p>Novel 페이지 결과의 크기 관련 정보를 캡슐화한 내부 레코드</p>
     * @param originalSize 원본(데이터 조회 시 리포지토리로부터 받은 전혀 가공되지 않은 결과)의 크기 (List.size())
     * @param limit 클라이언트로부터 전달받은 limit (역시 임의로 증감시키거나 감소시키지 않은 원래 값)
     */
    private record PageSizeInfo(int originalSize, int limit) {

        public boolean hasNextPage() {
            return originalSize > limit;
        }
    }
}
