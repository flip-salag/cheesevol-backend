package com.iucyh.novelservice.novel.repository.query;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.query.dto.NovelQueryDto;
import com.iucyh.novelservice.novel.repository.query.condition.NovelSearchCondition;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;

import java.util.List;

public interface NovelQueryRepository {

    List<? extends NovelQueryDto> findNovels(NovelSearchCondition condition, NovelPagingStrategy strategy);
    List<? extends NovelQueryDto> findNovelsByCategory(NovelSearchCondition condition, NovelPagingStrategy strategy, NovelCategory category);

    /**
     * 이번달 신작 소설 조회 메서드
     */
    List<? extends NovelQueryDto> findNewNovels(NovelSearchCondition condition, NovelPagingStrategy strategy);
}
