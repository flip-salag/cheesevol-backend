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
        return novel.periodViewCount.lt(popularCursor.periodViewCount())
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
    }

    @Override
    protected BooleanExpression applyAdditionalFilter() {
        // 정렬의 안정성을 위해 정합성이 깨진(회차는 존재하는데 최신 회차 발행일은 null인) 소설 제외
        return novel.lastEpisodePublishDate.isNotNull();
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
