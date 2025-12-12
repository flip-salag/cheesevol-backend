package com.iucyh.novelservice.novel.repository.query.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelPopularCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.paging.template.AbstractNovelPagingStrategy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelPopularPagingStrategy extends AbstractNovelPagingStrategy {

    @Override
    protected OrderSpecifier<?>[] applyOrder() {
        return new OrderSpecifier[] {
                novel.periodViewCount.desc(),
                novel.totalViewCount.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelPopularCursor popularCursor = (NovelPopularCursor) cursor;
        return novel.periodViewCount.lt(popularCursor.lastPeriodViewCount())
                .or(
                        novel.periodViewCount.eq(popularCursor.lastPeriodViewCount())
                                .and(
                                        novel.totalViewCount.lt(popularCursor.lastTotalViewCount())
                                )
                )
                .or(
                        novel.periodViewCount.eq(popularCursor.lastPeriodViewCount())
                                .and(
                                        novel.totalViewCount.eq(popularCursor.lastTotalViewCount())
                                )
                                .and(
                                        novel.id.lt(popularCursor.lastNovelId())
                                )
                );
    }

    @Override
    public NovelCursor createCursor(Novel lastResult) {
        return new NovelPopularCursor(lastResult.getId(), lastResult.getPeriodViewCount(), lastResult.getTotalViewCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.POPULAR;
    }
}
