package com.iucyh.novelservice.novel.repository.query.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelLikeCountCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.paging.template.AbstractNovelPagingStrategy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelLikeCountPagingStrategy extends AbstractNovelPagingStrategy {

    @Override
    protected OrderSpecifier<?>[] applyOrder() {
        return new OrderSpecifier[] {
                novel.likeCount.desc(),
                novel.lastPublishedAt.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelLikeCountCursor likeCountCursor = (NovelLikeCountCursor) cursor;
        return novel.likeCount.lt(likeCountCursor.lastLikeCount())
                .or(
                        novel.likeCount.eq(likeCountCursor.lastLikeCount())
                                .and(
                                        novel.lastPublishedAt.lt(likeCountCursor.lastPublishedAt())
                                )
                )
                .or(
                        novel.likeCount.eq(likeCountCursor.lastLikeCount())
                                .and(
                                        novel.lastPublishedAt.eq(likeCountCursor.lastPublishedAt())
                                )
                                .and(
                                        novel.id.lt(likeCountCursor.lastNovelId())
                                )
                );
    }

    @Override
    public NovelCursor createCursor(Novel lastResult) {
        return new NovelLikeCountCursor(lastResult.getId(), lastResult.getLikeCount(), lastResult.getLastPublishedAt());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LIKE_COUNT;
    }
}
