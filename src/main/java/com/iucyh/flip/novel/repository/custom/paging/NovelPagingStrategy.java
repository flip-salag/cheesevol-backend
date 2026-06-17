package com.iucyh.flip.novel.repository.custom.paging;

import com.iucyh.flip.novel.domain.Novel;
import com.iucyh.flip.novel.enumtype.NovelSortType;
import com.iucyh.flip.novel.repository.custom.paging.cursor.NovelCursor;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * <p>Novel의 각 정렬 기준에 맞는 페이징 전/후처리 전략</p>
 */
public interface NovelPagingStrategy {

    /**
     * 주어진 query 에 페이징(orderBy, cursor 등)을 적용하는 메서드
     * @param query 페이징을 적용할 쿼리
     * @param cursor 페이지 조회에 필요한 커서
     * @return 페이징이 적용된 쿼리
     */
    JPAQuery<Novel> applyPaging(JPAQuery<Novel> query, NovelCursor cursor);

    /**
     * <p>해당 전략이 담당하는 정렬 기준에 맞는 {@code NovelCursor}를 생성하는 메서드</p>
     * <b>주의: 조회 시 사용한 전략을 그대로 사용해야 합니다. 다른 전략을 임의로 생성하거나 찾아서 쓸 경우 의도치 않은 동작이 발생할 수 있습니다.</b>
     * @param lastResult 조회된 결과의 마지막 요소
     * @return 생성된 {@code NovelCursor}
     */
    NovelCursor createCursor(Novel lastResult);

    /**
     * @return 해당 전략이 사용하는 {@code NovelCursor} 클래스
     */
    Class<? extends NovelCursor> getCursorClass();

    /**
     * @return 해당 전략이 담당하는 정렬 기준과 매핑되는 {@code NovelSortType}
     */
    NovelSortType getSupportedSortType();
}
