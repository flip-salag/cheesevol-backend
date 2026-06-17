package com.iucyh.flip.section.service;

import com.iucyh.flip.novel.domain.Novel;
import com.iucyh.flip.novel.enumtype.NovelCategory;
import com.iucyh.flip.novel.enumtype.NovelSortType;
import com.iucyh.flip.novel.repository.NovelRepository;
import com.iucyh.flip.novel.repository.custom.condition.NovelPagingCondition;
import com.iucyh.flip.novel.repository.custom.paging.NovelPagingStrategy;
import com.iucyh.flip.novel.service.registry.NovelPagingStrategyRegistry;
import com.iucyh.flip.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.flip.novel.web.dto.response.NovelSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionQueryService {

    private final NovelPagingStrategyRegistry pagingStrategyRegistry;
    private final NovelRepository novelRepository;

    public List<NovelSummaryResponse> getPopularNovels(NovelCategory category) {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 10);
        NovelPagingStrategy strategy = pagingStrategyRegistry.get(NovelSortType.POPULAR);
        List<Novel> novels = novelRepository.findNovels(pagingCondition, strategy, category);

        return mapToNovelResponseList(novels);
    }

    public List<NovelSummaryResponse> getNewNovels() {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 30);
        NovelPagingStrategy strategy = pagingStrategyRegistry.get(NovelSortType.LAST_UPDATE);
        List<Novel> novels = novelRepository.findNewNovels(pagingCondition, strategy, null);

        return mapToNovelResponseList(novels);
    }

    private List<NovelSummaryResponse> mapToNovelResponseList(List<Novel> novels) {
        return novels.stream()
                .map(NovelResponseMapper::toNovelSummaryResponse)
                .toList();
    }
}
