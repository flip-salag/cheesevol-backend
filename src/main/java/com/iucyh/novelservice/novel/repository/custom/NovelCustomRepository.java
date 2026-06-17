package com.iucyh.novelservice.novel.repository.custom;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.custom.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.custom.paging.NovelPagingStrategy;

import java.util.List;

public interface NovelCustomRepository {

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
     * <p>{@code userId}에 해당하는 유저가 작성한 소설 중 전달된 {@code title}과 중복되는 제목이 있는지 검사</p>
     * <b>삭제된 소설은 제외하고 검사, 만약 {@code publicId}가 null이 아니라면 그에 해당하는 소설도 제외하고 검사(업데이트 중인 소설을 제외하는 등의 상황에서 사용)</b>
     * @param title 검사할 제목
     * @param userId 기준이 될 유저의 pk
     * @param publicId 추가로 제외할 소설의 public id (선택, 모든 소설을 대상으로 검사 시 null 전달)
     * @return 중복되는 제목을 가진 소설이 존재하면 {@code true}, 아니라면 {@code false}
     */
    boolean novelTitleExistsByUserId(String title, Long userId, String publicId);

    /**
     * <p>Novel 목록 조회, category가 null이 아닐 시 해당 category에 해당하는 Novel만 조회</p>
     * <p>삭제되었거나(soft delete 포함), COMMON(일반) 회차가 한개도 존재하지 않는 소설은 제외</p>
     * @param condition 페이징 조건 (커서, limit 등)
     * @param strategy Novel의 정렬 기준에 따른 페이징 쿼리 생성 전략
     * @param category 필터링 할 카테고리, 모든 카테고리 조회 시 null 전달
     */
    List<Novel> findNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category);

    /**
     * <p>신작 Novel 목록 조회, category가 null이 아닐 시 해당 category에 해당하는 Novel만 조회</p>
     * <p>삭제되었거나(soft delete 포함), COMMON(일반) 회차가 한개도 존재하지 않는 소설은 제외</p>
     * @param condition 페이징 조건 (커서, limit 등)
     * @param strategy Novel의 정렬 기준에 따른 페이징 쿼리 생성 전략
     * @param category 필터링 할 카테고리, 모든 카테고리 조회 시 null 전달
     */
    List<Novel> findNewNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category);
}
