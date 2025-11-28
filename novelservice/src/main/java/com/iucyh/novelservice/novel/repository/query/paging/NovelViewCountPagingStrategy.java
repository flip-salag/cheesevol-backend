package com.iucyh.novelservice.novel.repository.query.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.dto.NovelQueryDto;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelViewCountCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.paging.template.AbstractNovelPagingStrategy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelViewCountPagingStrategy extends AbstractNovelPagingStrategy {

    @Override
    protected OrderSpecifier<?>[] applyOrder() {
        return new OrderSpecifier[] {
                novel.totalViewCount.desc(),
                novel.lastEpisodeAt.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelViewCountCursor viewCountCursor = (NovelViewCountCursor) cursor;
        return novel.totalViewCount.lt(viewCountCursor.lastTotalViewCount())
                .or(
                        novel.totalViewCount.eq(viewCountCursor.lastTotalViewCount())
                                .and(
                                        novel.lastEpisodeAt.lt(viewCountCursor.lastEpisodeAt())
                                )
                )
                .or(
                        novel.totalViewCount.eq(viewCountCursor.lastTotalViewCount())
                                .and(
                                        novel.lastEpisodeAt.eq(viewCountCursor.lastEpisodeAt())
                                )
                                .and(
                                        novel.id.lt(viewCountCursor.lastNovelId())
                                )
                );
    }

    @Override
    public NovelCursor createCursor(NovelQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelViewCountCursor(lastNovel.getId(), lastNovel.getTotalViewCount(), lastNovel.getLastEpisodeAt());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.VIEW_COUNT;
    }
}
