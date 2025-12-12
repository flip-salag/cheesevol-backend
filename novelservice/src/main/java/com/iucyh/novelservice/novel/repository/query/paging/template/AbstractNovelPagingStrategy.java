package com.iucyh.novelservice.novel.repository.query.paging.template;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * <p>가장 일반적인 패턴의 페이징 쿼리 생성을 돕는 Novel의 페이징 전략들을 위한 템플릿 클래스</p>
 * <p>{기본 쿼리}.{커서 적용}.{정렬} 패턴이라면 이 클래스 상속 권장</p>
 */
public abstract class AbstractNovelPagingStrategy implements NovelPagingStrategy {

    /**
     * <p>각 전략 별 정렬 기준 적용 메서드</p>
     */
    protected abstract OrderSpecifier<?>[] applyOrder();

    /**
     * <p>각 전략 별 커서 적용 메서드</p>
     */
    protected abstract BooleanExpression applyCursor(NovelCursor cursor);

    @Override
    public JPAQuery<Novel> applyPaging(JPAQuery<Novel> query, NovelCursor cursor) {
        query.orderBy(
                applyOrder()
        );
        if (isNotFirstPage(cursor)) {
            query.where(
                    applyCursor(cursor)
            );
        }
        return query;
    }

    private boolean isNotFirstPage(NovelCursor cursor) {
        return cursor != null;
    }
}
