package com.iucyh.novelservice.novel.repository.custom.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelLikeCountCursor;
import com.iucyh.novelservice.novel.repository.custom.paging.template.AbstractNovelPagingStrategy;
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
                novel.totalViewCount.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelLikeCountCursor likeCountCursor = (NovelLikeCountCursor) cursor;
        return novel.likeCount.lt(likeCountCursor.getLikeCount())
                .or(
                        novel.likeCount.eq(likeCountCursor.getLikeCount())
                                .and(
                                        novel.totalViewCount.lt(likeCountCursor.getTotalViewCount())
                                )
                )
                .or(
                        novel.likeCount.eq(likeCountCursor.getLikeCount())
                                .and(
                                        novel.totalViewCount.eq(likeCountCursor.getTotalViewCount())
                                )
                                .and(
                                        novel.id.lt(likeCountCursor.getNovelId())
                                )
                );
    }

    @Override
    public NovelCursor createCursor(Novel lastResult) {
        return NovelLikeCountCursor.of(lastResult.getId(), lastResult.getLikeCount(), lastResult.getTotalViewCount());
    }

    @Override
    public Class<? extends NovelCursor> getCursorClass() {
        return NovelLikeCountCursor.class;
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LIKE_COUNT;
    }
}
