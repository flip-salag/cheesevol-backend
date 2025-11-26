package com.iucyh.novelservice.novel.repository.query.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.dto.NovelQueryDto;
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
        NovelPopularCursor novelPopularCursor = (NovelPopularCursor) cursor;
        return novel.periodViewCount.lt(novelPopularCursor.lastPeriodViewCount())
                .or(
                        novel.periodViewCount.eq(novelPopularCursor.lastPeriodViewCount())
                                .and(
                                        novel.totalViewCount.lt(novelPopularCursor.lastTotalViewCount())
                                )
                )
                .or(
                        novel.periodViewCount.eq(novelPopularCursor.lastPeriodViewCount())
                                .and(
                                        novel.totalViewCount.eq(novelPopularCursor.lastTotalViewCount())
                                )
                                .and(
                                        novel.id.lt(novelPopularCursor.lastNovelId())
                                )
                );
    }

    @Override
    public NovelCursor createCursor(NovelQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelPopularCursor(lastNovel.getId(), lastNovel.getPeriodViewCount(), lastNovel.getTotalViewCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.POPULAR;
    }
}
