package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.common.response.PageResponse;
import com.iucyh.novelservice.novel.service.codec.NovelCursorCodec;
import com.iucyh.novelservice.novel.service.dto.query.GetNewNovelsQuery;
import com.iucyh.novelservice.novel.service.dto.query.GetNovelsQuery;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.response.NovelResponse;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.repository.query.NovelQueryRepository;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class NovelQueryService {

    private final NovelCursorCodec cursorCodec;
    private final NovelRepository novelRepository;
    private final NovelQueryRepository novelQueryRepository;
    private final Map<NovelSortType, NovelPagingStrategy> pagingStrategyMap;

    public NovelQueryService(
            NovelCursorCodec cursorCodec,
            NovelRepository novelRepository,
            NovelQueryRepository novelQueryRepository,
            List<NovelPagingStrategy> pagingStrategies
    ) {
        this.cursorCodec = cursorCodec;
        this.novelRepository = novelRepository;
        this.novelQueryRepository = novelQueryRepository;
        this.pagingStrategyMap = pagingStrategies
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                NovelPagingStrategy::getSupportedSortType,
                                Function.identity()
                        )
                );
    }

    public List<NovelResponse> getPopularNovelsForSection(NovelCategory category) {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 10);
        NovelPagingStrategy strategy = getPagingStrategy(NovelSortType.POPULAR);
        List<Novel> novels = novelQueryRepository.findNovels(pagingCondition, strategy, category);

        return mapToNovelResponseList(novels);
    }

    public List<NovelResponse> getNewNovelsForSection() {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 30);
        NovelPagingStrategy strategy = getPagingStrategy(NovelSortType.LAST_UPDATE);
        List<Novel> novels = novelQueryRepository.findNewNovels(pagingCondition, strategy, null);

        return mapToNovelResponseList(novels);
    }

    public PageResponse<NovelResponse> getNovels(GetNovelsQuery query) {
        return findNovels(query.sortType(), query.cursor(), query.limit(),
                (pagingCondition, strategy) ->
                        novelQueryRepository.findNovels(pagingCondition, strategy, query.category())
        );
    }

    public PageResponse<NovelResponse> getNewNovels(GetNewNovelsQuery query) {
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
    private PageResponse<NovelResponse> findNovels(
            NovelSortType sortType, String cursor, int limit,
            BiFunction<NovelPagingCondition, NovelPagingStrategy, List<Novel>> finder
    ) {
        NovelPagingCondition pagingCondition = createPagingCondition(sortType, cursor, limit + 1);
        NovelPagingStrategy pagingStrategy = getPagingStrategy(sortType);

        List<Novel> result = finder.apply(pagingCondition, pagingStrategy);
        // TODO: 각 조회 조건별 쿼리 세분화 필요, 쿼리 호출 최소화 필요
        long totalCount = novelRepository.countByDeletedAtIsNull();

        if (result.isEmpty()) {
            return NovelResponseMapper.toPageResponse(List.of(), totalCount, null);
        }

        List<Novel> pageResult = result.stream().limit(limit).toList();
        String newCursor = null;

        boolean hasNext = result.size() > limit;
        if (hasNext) {
            newCursor = createNewEncodedCursor(pagingStrategy, pageResult);
        }

        List<NovelResponse> novelResponses = mapToNovelResponseList(pageResult);
        return NovelResponseMapper.toPageResponse(novelResponses, totalCount, newCursor);
    }

    private NovelPagingStrategy getPagingStrategy(NovelSortType sortType) {
        NovelPagingStrategy pagingStrategy = pagingStrategyMap.get(sortType);
        if (pagingStrategy == null) {
            throw new IllegalArgumentException("There's no matched paging strategy with: " + sortType.name());
        }
        return pagingStrategy;
    }

    private NovelPagingCondition createPagingCondition(NovelSortType sortType, String encodedCursor, int limit) {
        NovelCursor decodedCursor = cursorCodec.decode(encodedCursor, sortType.getSupportedCursorClass());
        return new NovelPagingCondition(decodedCursor, limit);
    }

    private List<NovelResponse> mapToNovelResponseList(List<Novel> novels) {
        return novels.stream()
                .map(NovelResponseMapper::toNovelResponse)
                .toList();
    }

    private String createNewEncodedCursor(NovelPagingStrategy pagingStrategy, List<Novel> novels) {
        Novel lastResult = novels.get(novels.size() - 1);
        NovelCursor newCursor = pagingStrategy.createCursor(lastResult);
        return cursorCodec.encode(newCursor);
    }
}
