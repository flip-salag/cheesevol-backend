package com.iucyh.novelservice.novel.repository.query.paging.template;

import com.iucyh.novelservice.novel.repository.query.projection.NovelQueryProjection;
import com.iucyh.novelservice.novel.repository.query.projection.QNovelSimpleQueryProjection;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.iucyh.novelservice.novel.domain.QNovel.*;

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

    /**
     * <p>기본적인 조회 쿼리 생성 메서드</p>
     * <b>정렬 기준과 커서 적용은 제외</b>
     * <p>* 전략이 NovelSimpleQueryProjection 을 사용하지 않는 경우 오버라이딩 필요</p>
     */
    protected JPAQuery<? extends NovelQueryProjection> createBaseQuery(JPAQueryFactory queryFactory) {
        return queryFactory
                .select(new QNovelSimpleQueryProjection(novel))
                .from(novel);
    }

    @Override
    public JPAQuery<? extends NovelQueryProjection> createQuery(JPAQueryFactory queryFactory, NovelCursor cursor) {
        JPAQuery<? extends NovelQueryProjection> query = createBaseQuery(queryFactory)
                .orderBy(
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
