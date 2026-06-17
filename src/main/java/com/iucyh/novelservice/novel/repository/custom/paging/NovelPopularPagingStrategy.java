package com.iucyh.novelservice.novel.repository.custom.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelPopularCursor;
import com.iucyh.novelservice.novel.repository.custom.paging.template.AbstractNovelPagingStrategy;
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
                novel.lastEpisodePublishDate.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelPopularCursor popularCursor = (NovelPopularCursor) cursor;
        return novel.periodViewCount.lt(popularCursor.getPeriodViewCount())
                .or(
                        novel.periodViewCount.eq(popularCursor.getPeriodViewCount())
                                .and(
                                        novel.lastEpisodePublishDate.lt(popularCursor.getLastEpisodePublishDate())
                                )
                )
                .or(
                        novel.periodViewCount.eq(popularCursor.getPeriodViewCount())
                                .and(
                                        novel.lastEpisodePublishDate.eq(popularCursor.getLastEpisodePublishDate())
                                )
                                .and(
                                        novel.id.lt(popularCursor.getNovelId())
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
        return NovelPopularCursor.of(lastResult.getId(), lastResult.getPeriodViewCount(), lastResult.getLastEpisodePublishDate());
    }

    @Override
    public Class<? extends NovelCursor> getCursorClass() {
        return NovelPopularCursor.class;
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.POPULAR;
    }
}
