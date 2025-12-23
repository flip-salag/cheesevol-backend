package com.iucyh.novelservice.novel.repository.query;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;

import java.util.List;

public interface NovelQueryRepository {

    /**
     * <p>아래 조건들을 충족하는 소설이 존재하는지 검사</p>
     * <ul>
     *     <li>{@code publicId}에 해당하는 소설</li>
     *     <li>삭제되지 않은 소설</li>
     * </ul>
     * @param publicId 검사할 소설의 public id
     * @return 조건을 충족하는 소설이 존재하면 {@code true}, 아니라면 {@code false}
     */
    boolean existsByPublicId(String publicId);

    /**
     * @param category 필터링 할 카테고리, 모든 카테고리 조회 시 null 전달
     */
    List<Novel> findNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category);

    /**
     * 이번달 신작 소설 조회 메서드
     * @param category 필터링 할 카테고리, 모든 카테고리 조회 시 null 전달
     */
    List<Novel> findNewNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category);
}
