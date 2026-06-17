package com.iucyh.novelservice.novel.repository.custom.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelLastUpdateCursor;
import com.iucyh.novelservice.novel.repository.custom.paging.template.AbstractNovelPagingStrategy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelLastUpdatePagingStrategy extends AbstractNovelPagingStrategy {

    @Override
    protected OrderSpecifier<?>[] applyOrder() {
        return new OrderSpecifier[] {
                novel.lastEpisodePublishDate.desc(),
                novel.totalViewCount.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelLastUpdateCursor lastUpdateCursor = (NovelLastUpdateCursor) cursor;
        return novel.lastEpisodePublishDate.lt(lastUpdateCursor.getLastEpisodePublishDate())
                .or(
                        novel.lastEpisodePublishDate.eq(lastUpdateCursor.getLastEpisodePublishDate())
                                .and(
                                        novel.totalViewCount.lt(lastUpdateCursor.getTotalViewCount())
                                )
                )
                .or(
                        novel.lastEpisodePublishDate.eq(lastUpdateCursor.getLastEpisodePublishDate())
                                .and(
                                        novel.totalViewCount.eq(lastUpdateCursor.getTotalViewCount())
                                )
                                .and(
                                        novel.id.lt(lastUpdateCursor.getNovelId())
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
        return NovelLastUpdateCursor.of(lastResult.getId(), lastResult.getLastEpisodePublishDate(), lastResult.getTotalViewCount());
    }

    @Override
    public Class<? extends NovelCursor> getCursorClass() {
        return NovelLastUpdateCursor.class;
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LAST_UPDATE;
    }
}
