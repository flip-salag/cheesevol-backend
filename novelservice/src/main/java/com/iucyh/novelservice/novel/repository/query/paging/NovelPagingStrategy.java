package com.iucyh.novelservice.novel.repository.query.paging;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.querydsl.jpa.impl.JPAQuery;

public interface NovelPagingStrategy {

    /**
     * 주어진 query 에 페이징(orderBy, cursor 등)을 적용하는 메서드
     * @param query 페이징을 적용할 쿼리
     * @param cursor 페이지 조회에 필요한 커서
     * @return 페이징이 적용된 쿼리
     */
    JPAQuery<Novel> applyPaging(JPAQuery<Novel> query, NovelCursor cursor);
    NovelCursor createCursor(Novel lastResult);
    NovelSortType getSupportedSortType();
}
