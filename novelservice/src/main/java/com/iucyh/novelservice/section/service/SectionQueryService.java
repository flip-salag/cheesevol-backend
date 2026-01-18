package com.iucyh.novelservice.section.service;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.NovelQueryRepository;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import com.iucyh.novelservice.novel.service.registry.NovelPagingStrategyRegistry;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.response.NovelSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionQueryService {

    private final NovelPagingStrategyRegistry pagingStrategyFactory;
    private final NovelQueryRepository novelQueryRepository;

    public List<NovelSummaryResponse> getPopularNovels(NovelCategory category) {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 10);
        NovelPagingStrategy strategy = pagingStrategyFactory.get(NovelSortType.POPULAR);
        List<Novel> novels = novelQueryRepository.findNovels(pagingCondition, strategy, category);

        return mapToNovelResponseList(novels);
    }

    public List<NovelSummaryResponse> getNewNovels() {
        NovelPagingCondition pagingCondition = new NovelPagingCondition(null, 30);
        NovelPagingStrategy strategy = pagingStrategyFactory.get(NovelSortType.LAST_UPDATE);
        List<Novel> novels = novelQueryRepository.findNewNovels(pagingCondition, strategy, null);

        return mapToNovelResponseList(novels);
    }

    private List<NovelSummaryResponse> mapToNovelResponseList(List<Novel> novels) {
        return novels.stream()
                .map(NovelResponseMapper::toNovelSummaryResponse)
                .toList();
    }
}
