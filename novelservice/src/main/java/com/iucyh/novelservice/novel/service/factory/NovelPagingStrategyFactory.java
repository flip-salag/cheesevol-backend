package com.iucyh.novelservice.novel.service.factory;

import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Novel의 페이징 전략을 조회하는 Factory</p>
 * <b>빈 생성 시 특정 NovelSortType과 매칭되는 전략이 없으면 {@code IllegalStateException} 발생</b>
 */
@Component
public class NovelPagingStrategyFactory {

    private final Map<NovelSortType, NovelPagingStrategy> pagingStrategyMap;

    public NovelPagingStrategyFactory(List<NovelPagingStrategy> pagingStrategies) {
        this.pagingStrategyMap = pagingStrategies
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                NovelPagingStrategy::getSupportedSortType,
                                Function.identity()
                        )
                );
    }

    @PostConstruct
    private void validate() { // 각 NovelSortType에 매핑되는 전략이 존재하는지 검증(하나라도 없다면 에러)
        for (NovelSortType sortType : NovelSortType.values()) {
            if (!pagingStrategyMap.containsKey(sortType)) {
                throw new IllegalStateException("There's no matched paging strategy with: " + sortType.name());
            }
        }
    }

    /**
     * <p>{@code sortType}과 매칭되는 페이징 전략 조회</p>
     * <b>빈 초기화 단계에서 각 {@code NovelSortType}과 매칭되는 전략이 있는지 검증하므로 null은 절대 반환되지 않음</b>
     * @param sortType 조회 기준
     * @return 전달된 {@code sortType}과 매칭되는 {@code NovelPagingStrategy}
     */
    public NovelPagingStrategy get(NovelSortType sortType) {
        return pagingStrategyMap.get(sortType);
    }
}
