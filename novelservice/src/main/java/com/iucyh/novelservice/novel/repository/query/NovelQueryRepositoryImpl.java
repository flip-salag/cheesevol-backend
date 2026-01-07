package com.iucyh.novelservice.novel.repository.query;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.query.condition.NovelPagingCondition;
import com.iucyh.novelservice.novel.repository.query.paging.NovelPagingStrategy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;
import static com.iucyh.novelservice.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class NovelQueryRepositoryImpl implements NovelQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByPublicId(String publicId) {
        Integer result = queryFactory
                .selectOne()
                .from(novel)
                .where(
                        novel.publicId.eq(publicId),
                        novel.deletedAt.isNull()
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public boolean novelTitleExistsByUserId(String title, Long userId, String publicId) {
        Integer result = queryFactory
                .selectOne()
                .from(novel)
                .where(
                        novel.title.eq(title),
                        novel.user.id.eq(userId),
                        publicId == null ? null : novel.publicId.ne(publicId),
                        novel.deletedAt.isNull()
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public List<Novel> findNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        JPAQuery<Novel> query = queryFactory
                .selectFrom(novel)
                .join(novel.user, user).fetchJoin()
                .where(
                        applyValidNovelFilter(),
                        applyCategoryFilter(category)
                )
                .limit(condition.limit());

        return strategy
                .applyPaging(query, condition.cursor())
                .fetch();
    }

    @Override
    public List<Novel> findNewNovels(NovelPagingCondition condition, NovelPagingStrategy strategy, NovelCategory category) {
        LocalDateTime thisMonth = getThisMonth();
        JPAQuery<Novel> query = queryFactory
                .selectFrom(novel)
                .join(novel.user, user).fetchJoin()
                .where(
                        applyValidNovelFilter(),
                        applyCategoryFilter(category),
                        novel.createdAt.goe(thisMonth)
                )
                .limit(condition.limit());

        return strategy
                .applyPaging(query, condition.cursor())
                .fetch();
    }

    /**
     * <p>조회할 소설이 유효한 소설인지 검사하기 위한 필터 적용</p>
     * <p>필터 조건</p>
     * <ul>
     *     <li>삭제되지 않은 소설</li>
     *     <li>회차가 한개라도 존재하는(== 최신 회차 등록일이 null이 아닌) 소설</li>
     * </ul>
     */
    private BooleanExpression applyValidNovelFilter() {
        return novel.deletedAt.isNull()
                .and(novel.lastPublishedAt.isNotNull());
    }

    private BooleanExpression applyCategoryFilter(NovelCategory category) {
        return category == null ? null : novel.category.eq(category);
    }

    private LocalDateTime getThisMonth() {
        return LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
