package com.iucyh.novelservice.novel.repository.query.paging;

import com.iucyh.novelservice.novel.repository.query.projection.NovelQueryProjection;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface NovelPagingStrategy {

    JPAQuery<? extends NovelQueryProjection> createQuery(JPAQueryFactory queryFactory, NovelCursor cursor);
    NovelCursor createCursor(NovelQueryProjection lastResult);
    NovelSortType getSupportedSortType();
}
