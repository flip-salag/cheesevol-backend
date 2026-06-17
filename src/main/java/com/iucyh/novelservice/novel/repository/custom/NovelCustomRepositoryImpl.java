package com.iucyh.novelservice.novel.repository.custom;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.custom.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.custom.paging.NovelPagingStrategy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;
import static com.iucyh.novelservice.user.domain.QUser.user;

@RequiredArgsConstructor
public class NovelCustomRepositoryImpl implements NovelCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByPublicId(String publicId) {
        Integer result = queryFactory
                .selectOne()
                .from(novel)
                .where(
                        novel.publicId.eq(publicId),
                        novel.deletedAt.isNull()
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public boolean novelTitleExistsByUserId(String title, Long userId, String publicId) {
        Integer result = queryFactory
                .selectOne()
                .from(novel)
                .where(
                        novel.title.eq(title),
                        novel.user.id.eq(userId),
                        publicId == null ? null : novel.publicId.ne(publicId),
                        novel.deletedAt.isNull()
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public List<Novel> findNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        JPAQuery<Novel> query = queryFactory
                .selectFrom(novel)
                .join(novel.user, user).fetchJoin()
                .where(
                        applyValidNovelFilter(),
                        applyCategoryFilter(category)
                )
                .limit(condition.limit());

        return strategy
                .applyPaging(query, condition.cursor())
                .fetch();
    }

    @Override
    public List<Novel> findNewNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        JPAQuery<Novel> query = queryFactory
                .selectFrom(novel)
                .join(novel.user, user).fetchJoin()
                .where(
                        applyValidNovelFilter(),
                        applyCategoryFilter(category),
                        applyNewNovelFilter()
                )
                .limit(condition.limit());

        return strategy
                .applyPaging(query, condition.cursor())
                .fetch();
    }

    /**
     * <p>조회할 소설이 유효한 소설인지 검사하기 위한 필터 적용</p>
     * <p>필터 조건</p>
     * <ul>
     *     <li>삭제되지 않은 소설</li>
     *     <li>일반 회차가 한개라도 존재하는 소설</li>
     * </ul>
     */
    private BooleanExpression applyValidNovelFilter() {
        return novel.deletedAt.isNull()
                .and(novel.commonEpisodeCount.gt(0));
    }

    private BooleanExpression applyCategoryFilter(NovelCategory category) {
        return category == null ? null : novel.category.eq(category);
    }

    /**
     * <p>신작 소설만 조회하기 위한 필터 적용</p>
     */
    private BooleanExpression applyNewNovelFilter() {
        LocalDateTime now = LocalDateTime.now();
        return novel.publishedAt.goe(
                now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN)
        ).and(
                novel.publishedAt.lt(
                        now.with(TemporalAdjusters.firstDayOfNextMonth()).with(LocalTime.MIN)
                )
        );
    }
}
