package com.iucyh.novelservice.novel.repository.query;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.query.projection.NovelQueryProjection;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Repository
@RequiredArgsConstructor
public class NovelQueryRepositoryImpl implements NovelQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<? extends NovelQueryProjection> findNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        return strategy
                .createQuery(queryFactory, condition.cursor())
                .where(
                        applyDefaultFilter(),
                        applyCategoryFilter(category)
                )
                .limit(condition.limit())
                .fetch();
    }

    @Override
    public List<? extends NovelQueryProjection> findNewNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        LocalDateTime thisMonth = getThisMonth();
        return strategy
                .createQuery(queryFactory, condition.cursor())
                .where(
                        applyDefaultFilter(),
                        applyCategoryFilter(category),
                        novel.createdAt.goe(thisMonth)
                )
                .limit(condition.limit())
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
