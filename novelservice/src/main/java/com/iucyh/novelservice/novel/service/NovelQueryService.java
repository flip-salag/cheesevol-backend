package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.common.response.PageWithCursorResponse;
import com.iucyh.novelservice.novel.service.codec.NovelCursorCodec;
import com.iucyh.novelservice.novel.service.dto.query.GetNewNovelsQuery;
import com.iucyh.novelservice.novel.service.dto.query.GetNovelsQuery;
import com.iucyh.novelservice.novel.service.factory.NovelPagingStrategyFactory;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
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
    private final NovelPagingStrategyFactory pagingStrategyFactory;
    private final NovelQueryRepository novelQueryRepository;

    public List<NovelSummaryResponse> getPopularNovelsForSection(NovelCategory category) {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 10);
        NovelPagingStrategy strategy = pagingStrategyFactory.get(NovelSortType.POPULAR);
        List<Novel> novels = novelQueryRepository.findNovels(pagingCondition, strategy, category);

        return mapToNovelResponseList(novels);
    }

    public List<NovelSummaryResponse> getNewNovelsForSection() {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 30);
        NovelPagingStrategy strategy = pagingStrategyFactory.get(NovelSortType.LAST_UPDATE);
        List<Novel> novels = novelQueryRepository.findNewNovels(pagingCondition, strategy, null);

        return mapToNovelResponseList(novels);
    }

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

    /**
     * <p>Novel Page 조회를 위한 공통 메서드</p>
     * @param finder 각 조회 종류별로 필요한 메서드 호출 로직을 담는 람다식
     *               <br>
     *               e.g) 조회 종류별 리포지토리 메서드 호출, 특정 비즈니스 로직을 위한 메서드 호출 및 조건 검사 등
     * @return 최종 결과를 담은 {@code PageResponse<NovelResponse>}
     */
    private PageWithCursorResponse<NovelSummaryResponse> findNovels(
            NovelSortType sortType, String cursor, int limit,
            BiFunction<NovelPagingCondition, NovelPagingStrategy, List<Novel>> finder
    ) {
        NovelPagingCondition pagingCondition = createPagingCondition(sortType, cursor, limit + 1);
        NovelPagingStrategy pagingStrategy = pagingStrategyFactory.get(sortType);

        List<Novel> result = finder.apply(pagingCondition, pagingStrategy);

        if (result.isEmpty()) {
            return NovelResponseMapper.toPageResponse(List.of(), null, limit);
        }

        List<Novel> pageResult = result.stream().limit(limit).toList();
        String newCursor = null;

        boolean hasNext = result.size() > limit;
        if (hasNext) {
            newCursor = createNewEncodedCursor(pagingStrategy, pageResult);
        }

        List<NovelSummaryResponse> novels = mapToNovelResponseList(pageResult);
        return NovelResponseMapper.toPageResponse(novels, newCursor, limit);
    }

    private NovelPagingCondition createPagingCondition(NovelSortType sortType, String encodedCursor, int limit) {
        NovelCursor decodedCursor = cursorCodec.decode(encodedCursor, sortType.getSupportedCursorClass());
        return new NovelPagingCondition(decodedCursor, limit);
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
