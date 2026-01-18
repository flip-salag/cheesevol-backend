package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.common.exception.DataNotFound;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.common.response.PageWithCursorResponse;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.service.codec.NovelCursorCodec;
import com.iucyh.novelservice.novel.service.dto.query.GetNewNovelsQuery;
import com.iucyh.novelservice.novel.service.dto.query.GetNovelsQuery;
import com.iucyh.novelservice.novel.service.registry.NovelPagingStrategyRegistry;
import com.iucyh.novelservice.novel.service.policy.NovelPolicyValidator;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.response.NovelDetailResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelSummaryResponse;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.NovelQueryRepository;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
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
    private final NovelQueryRepository novelQueryRepository;

    private final NovelPolicyValidator novelPolicyValidator;

    public PageWithCursorResponse<NovelSummaryResponse> getNovels(GetNovelsQuery query) {
        return findNovels(query.sortType(), query.cursor(), query.limit(),
                (pagingCondition, strategy) ->
                        novelQueryRepository.findNovels(pagingCondition, strategy, query.category())
        );
    }

    public PageWithCursorResponse<NovelSummaryResponse> getNewNovels(GetNewNovelsQuery query) {
        return findNovels(query.sortType(), query.cursor(), query.limit(),
                (pagingCondition, strategy) ->
                        novelQueryRepository.findNewNovels(pagingCondition, strategy, query.category())
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
     *               e.g) 조회 종류별 리포지토리 메서드 호출, 특정 비즈니스 로직을 위한 메서드 호출 및 조건 검사 등
     * @return 최종 결과를 담은 {@code PageWithCursorResponse<NovelSummaryResponse>}
     */
    private PageWithCursorResponse<NovelSummaryResponse> findNovels(
            NovelSortType sortType, String cursor, int limit,
            BiFunction<NovelPagingCondition, NovelPagingStrategy, List<Novel>> finder
    ) {
        NovelPagingStrategy pagingStrategy = pagingStrategyRegistry.get(sortType);
        NovelCursor decodedCursor = null;

        if (cursor != null && !cursor.isBlank()) {
            decodedCursor = cursorCodec.decode(cursor, pagingStrategy.getCursorClass());
            // JSON은 필드 순서를 신경쓰지 않기 때문에 필드명만 같다면 서로 다른 정렬 기준끼리도 커서가 호환될 수 있으므로 별도로 추가 검증
            novelPolicyValidator.validateNovelCursorMatchesSortType(decodedCursor, sortType);
        }

        // 다음 페이지가 있는지 확인하기 위해 limit + 1개 만큼 가져오기(결과의 size가 limit + 1 이라면 다음 페이지 존재)
        NovelPagingCondition pagingCondition = new NovelPagingCondition(decodedCursor, limit + 1);
        List<Novel> result = finder.apply(pagingCondition, pagingStrategy);

        if (result.isEmpty()) {
            return NovelResponseMapper.toPageResponse(List.of(), null, limit);
        }

        List<Novel> pageResult = result.stream().limit(limit).toList();
        String newCursor = null;

        boolean hasNext = result.size() > limit;
        if (hasNext) { // 다음에 가져올 데이터가 존재할 때(다음 페이지가 존재할 때)만 새 cursor 생성
            newCursor = createNewEncodedCursor(pagingStrategy, pageResult);
        }

        List<NovelSummaryResponse> novels = mapToNovelResponseList(pageResult);
        return NovelResponseMapper.toPageResponse(novels, newCursor, limit);
    }

    private List<NovelSummaryResponse> mapToNovelResponseList(List<Novel> novels) {
        return novels.stream()
                .map(NovelResponseMapper::toNovelSummaryResponse)
                .toList();
    }

    private String createNewEncodedCursor(NovelPagingStrategy pagingStrategy, List<Novel> novels) {
        Novel lastResult = novels.get(novels.size() - 1);
        NovelCursor newCursor = pagingStrategy.createCursor(lastResult);
        return cursorCodec.encode(newCursor);
    }
}
