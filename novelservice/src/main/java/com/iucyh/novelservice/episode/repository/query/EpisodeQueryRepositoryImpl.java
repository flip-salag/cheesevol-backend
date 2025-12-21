package com.iucyh.novelservice.episode.repository.query;

import com.iucyh.novelservice.episode.enumtype.EpisodeSortType;
import com.iucyh.novelservice.episode.repository.query.condition.EpisodePagingCondition;
import com.iucyh.novelservice.episode.repository.query.projection.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.iucyh.novelservice.episode.domain.QEpisode.episode;
import static com.iucyh.novelservice.novel.domain.QNovel.novel;
import static com.iucyh.novelservice.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class EpisodeQueryRepositoryImpl implements EpisodeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public LocalDateTime findLastEpisodeAtExceptDeletedEpisode(Long novelId, String publicId) {
        return queryFactory
                .select(episode.createdAt)
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        publicId == null ? null : episode.publicId.ne(publicId),
                        episode.deletedAt.isNull()
                )
                .orderBy(
                        episode.episodeNumber.desc()
                )
                .fetchFirst();
    }

    @Override
    public Optional<EpisodePrevNextQueryProjection> findPrevEpisode(Long novelId, Integer episodeNumber) {
        EpisodePrevNextQueryProjection result = queryFactory
                .select(Projections.constructor(
                        EpisodePrevNextQueryProjection.class,
                        episode.publicId,
                        episode.episodeNumber
                ))
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        episode.episodeNumber.lt(episodeNumber),
                        episode.deletedAt.isNull()
                )
                .orderBy(
                        episode.episodeNumber.desc()
                )
                .fetchFirst();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<EpisodePrevNextQueryProjection> findNextEpisode(Long novelId, Integer episodeNumber) {
        EpisodePrevNextQueryProjection result = queryFactory
                .select(Projections.constructor(
                        EpisodePrevNextQueryProjection.class,
                        episode.publicId,
                        episode.episodeNumber
                ))
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        episode.episodeNumber.gt(episodeNumber),
                        episode.deletedAt.isNull()
                )
                .orderBy(
                        episode.episodeNumber.asc()
                )
                .fetchFirst();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<EpisodeDetailQueryProjection> findEpisodeDetailByPublicId(String publicId) {
        EpisodeDetailQueryProjection result = queryFactory
                .select(
                        new QEpisodeDetailQueryProjection(
                                episode.publicId,
                                episode.title,
                                episode.description,
                                episode.episodeNumber,
                                episode.createdAt,

                                novel.id,
                                novel.publicId,
                                novel.title,
                                novel.likeCount,

                                user.publicId,
                                user.nickname
                        )
                )
                .from(episode)
                .join(episode.novel, novel)
                .join(novel.user, user)
                .where(
                        episode.publicId.eq(publicId),
                        applyValidEpisodeFilter()
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<String> findEpisodeContentByPublicId(String publicId) {
        String result = queryFactory
                .select(episode.content)
                .from(episode)
                .join(episode.novel, novel)
                .join(novel.user, user)
                .where(
                        episode.publicId.eq(publicId),
                        applyValidEpisodeFilter()
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Page<EpisodeSummaryQueryProjection> findEpisodesByNovelPublicId(String novelPublicId, EpisodePagingCondition condition) {
        Pageable pageable = condition.pageable();
        List<EpisodeSummaryQueryProjection> content = queryFactory
                .select(
                        new QEpisodeSummaryQueryProjection(
                                episode.publicId,
                                episode.title,
                                episode.description,
                                episode.viewCount,
                                episode.episodeNumber,
                                episode.createdAt
                        )
                )
                .from(episode)
                .join(episode.novel, novel)
                .where(
                        novel.publicId.eq(novelPublicId),
                        episode.deletedAt.isNull()
                )
                .orderBy(
                        applyOrder(condition.sortType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(episode.count())
                .from(episode)
                .join(episode.novel, novel)
                .where(
                        novel.publicId.eq(novelPublicId),
                        episode.deletedAt.isNull()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<Integer> applyOrder(EpisodeSortType sortType) {
        return sortType == EpisodeSortType.ASC ? episode.episodeNumber.asc() : episode.episodeNumber.desc();
    }

    /**
     * <p>조회할 회차가 유효한 회차인지 검사하기 위한 필터 적용</p>
     * <p>필터 조건</p>
     * <ul>
     *     <li>삭제되지 않은 회차</li>
     *     <li>회차가 속한 소설이 삭제되지 않은 상태</li>
     *     <li>회차가 속한 소설의 작성자가 삭제되지 않은 상태</li>
     * </ul>
     * <b>주의: novel, user와 관련된 조건을 사용하므로 쿼리에서 novel, user JOIN 필수</b>
     */
    private BooleanExpression applyValidEpisodeFilter() {
        return episode.deletedAt.isNull()
                .and(novel.deletedAt.isNull())
                .and(user.deletedAt.isNull());
    }
}
