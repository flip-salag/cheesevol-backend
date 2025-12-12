package com.iucyh.novelservice.novel.repository.query;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;
import static com.iucyh.novelservice.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class NovelQueryRepositoryImpl implements NovelQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Novel> findNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        JPAQuery<Novel> query = queryFactory
                .selectFrom(novel)
                .join(novel.user, user).fetchJoin()
                .where(
                        applyDefaultFilter(),
                        applyCategoryFilter(category)
                )
                .limit(condition.limit());

        return strategy
                .applyPaging(query, condition.cursor())
                .fetch();
    }

    @Override
    public List<Novel> findNewNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        LocalDateTime thisMonth = getThisMonth();
        JPAQuery<Novel> query = queryFactory
                .selectFrom(novel)
                .join(novel.user, user).fetchJoin()
                .where(
                        applyDefaultFilter(),
                        applyCategoryFilter(category),
                        novel.createdAt.goe(thisMonth)
                )
                .limit(condition.limit());

        return strategy
                .applyPaging(query, condition.cursor())
                .fetch();
    }

    private BooleanExpression applyDefaultFilter() {
        return novel.deletedAt.isNull() // 삭제되지 않은 소설
                .and(novel.lastEpisodeAt.isNotNull()); // 회차가 하나라도 존재하는 소설
    }

    private BooleanExpression applyCategoryFilter(NovelCategory category) {
        return category == null ? null : novel.category.eq(category);
    }

    private LocalDateTime getThisMonth() {
        return LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
