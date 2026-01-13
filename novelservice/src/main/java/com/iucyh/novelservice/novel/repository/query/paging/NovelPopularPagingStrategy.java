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
                novel.lastEpisodePublishDate.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression applyCursor(NovelCursor cursor) {
        NovelPopularCursor popularCursor = (NovelPopularCursor) cursor;
        BooleanExpression cursorCondition = novel.periodViewCount.lt(popularCursor.periodViewCount())
                .or(
                        novel.periodViewCount.eq(popularCursor.periodViewCount())
                                .and(
                                        novel.lastEpisodePublishDate.lt(popularCursor.lastEpisodePublishDate())
                                )
                )
                .or(
                        novel.periodViewCount.eq(popularCursor.periodViewCount())
                                .and(
                                        novel.lastEpisodePublishDate.eq(popularCursor.lastEpisodePublishDate())
                                )
                                .and(
                                        novel.id.lt(popularCursor.novelId())
                                )
                );
        return cursorCondition
                .and(novel.lastEpisodePublishDate.isNotNull()); // 정렬의 안정성을 위해 last_episode_publish_date 이 null인 소설은 제외
    }

    @Override
    public NovelCursor createCursor(Novel lastResult) {
        return new NovelPopularCursor(lastResult.getId(), lastResult.getPeriodViewCount(), lastResult.getLastEpisodePublishDate());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.POPULAR;
    }
}
