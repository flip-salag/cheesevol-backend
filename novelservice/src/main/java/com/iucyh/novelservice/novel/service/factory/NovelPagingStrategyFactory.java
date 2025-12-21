package com.iucyh.novelservice.novel.service.factory;

import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Novel의 페이징 전략을 조회하는 Factory</p>
 * <b>빈 생성 시 특정 NovelSortType과 매칭되는 전략이 없으면 에러 발생</b>
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
    private void validate() {
        for (NovelSortType sortType : NovelSortType.values()) {
            if (!pagingStrategyMap.containsKey(sortType)) {
                throw new IllegalStateException("There's no matched paging strategy with: " + sortType.name());
            }
        }
    }

    public NovelPagingStrategy get(NovelSortType sortType) {
        return pagingStrategyMap.get(sortType);
    }
}
