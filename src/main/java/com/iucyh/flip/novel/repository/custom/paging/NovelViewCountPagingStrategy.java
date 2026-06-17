package com.iucyh.flip.novel.repository.custom.paging;

import com.iucyh.flip.novel.domain.Novel;
import com.iucyh.flip.novel.enumtype.NovelSortType;
import com.iucyh.flip.novel.repository.custom.paging.cursor.NovelCursor;
import com.iucyh.flip.novel.repository.custom.paging.cursor.NovelViewCountCursor;
import com.iucyh.flip.novel.repository.custom.paging.template.AbstractNovelPagingStrategy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.iucyh.flip.novel.domain.QNovel.novel;

@Component
public class NovelViewCountPagingStrategy extends AbstractNovelPagingStrategy {

    @Override
    protected OrderSpecifier<?>[] applyOrder() {
        return new OrderSpecifier[] {
                novel.totalViewCount.desc(),
                novel.lastEpisodePublishDate.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelViewCountCursor viewCountCursor = (NovelViewCountCursor) cursor;
        return novel.totalViewCount.lt(viewCountCursor.getTotalViewCount())
                .or(
                        novel.totalViewCount.eq(viewCountCursor.getTotalViewCount())
                                .and(
                                        novel.lastEpisodePublishDate.lt(viewCountCursor.getLastEpisodePublishDate())
                                )
                )
                .or(
                        novel.totalViewCount.eq(viewCountCursor.getTotalViewCount())
                                .and(
                                        novel.lastEpisodePublishDate.eq(viewCountCursor.getLastEpisodePublishDate())
                                )
                                .and(
                                        novel.id.lt(viewCountCursor.getNovelId())
                                )
                );
    }

    @Override
    protected BooleanExpression applyAdditionalFilter() {
        // 정렬의 안정성을 위해 정합성이 깨진(회차는 존재하는데 최신 회차 발행일은 null인) 소설 제외
        return novel.lastEpisodePublishDate.isNotNull();
    }

    @Override
    public NovelCursor createCursor(Novel lastResult) {
        return NovelViewCountCursor.of(lastResult.getId(), lastResult.getTotalViewCount(), lastResult.getLastEpisodePublishDate());
    }

    @Override
    public Class<? extends NovelCursor> getCursorClass() {
        return NovelViewCountCursor.class;
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.VIEW_COUNT;
    }
}
