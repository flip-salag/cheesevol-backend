package com.iucyh.novelservice.novel.repository.query.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.dto.NovelQueryDto;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelLastUpdateCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.paging.template.AbstractNovelPagingStrategy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelLastUpdatePagingStrategy extends AbstractNovelPagingStrategy {

    @Override
    protected OrderSpecifier<?>[] applyOrder() {
        return new OrderSpecifier[] {
                novel.lastEpisodeAt.desc(),
                novel.createdAt.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelLastUpdateCursor lastUpdateCursor = (NovelLastUpdateCursor) cursor;
        return novel.lastEpisodeAt.lt(lastUpdateCursor.lastEpisodeAt())
                .or(
                        novel.lastEpisodeAt.eq(lastUpdateCursor.lastEpisodeAt())
                                .and(
                                        novel.createdAt.lt(lastUpdateCursor.lastCreatedAt())
                                )
                )
                .or(
                        novel.lastEpisodeAt.eq(lastUpdateCursor.lastEpisodeAt())
                                .and(
                                        novel.createdAt.eq(lastUpdateCursor.lastCreatedAt())
                                )
                                .and(
                                        novel.id.lt(lastUpdateCursor.lastNovelId())
                                )
                );
    }

    @Override
    public NovelCursor createCursor(NovelQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelLastUpdateCursor(lastNovel.getId(), lastNovel.getLastEpisodeAt(), lastNovel.getCreatedAt());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LAST_UPDATE;
    }
}
