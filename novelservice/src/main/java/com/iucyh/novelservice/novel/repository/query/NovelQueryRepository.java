package com.iucyh.novelservice.novel.repository.query;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import com.iucyh.novelservice.novel.repository.query.projection.NovelSimpleQueryProjection;

import java.util.List;

public interface NovelQueryRepository {

    /**
     * @param category 필터링 할 카테고리, 모든 카테고리 조회 시 null 전달
     */
    List<? extends NovelSimpleQueryProjection> findNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category);

    /**
     * 이번달 신작 소설 조회 메서드
     * @param category 필터링 할 카테고리, 모든 카테고리 조회 시 null 전달
     */
    List<? extends NovelSimpleQueryProjection> findNewNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category);
}
