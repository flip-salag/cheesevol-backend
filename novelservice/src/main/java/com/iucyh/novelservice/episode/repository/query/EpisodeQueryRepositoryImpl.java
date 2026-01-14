package com.iucyh.novelservice.episode.repository.query;

import com.iucyh.novelservice.episode.enumtype.EpisodeSortType;
import com.iucyh.novelservice.episode.enumtype.EpisodeType;
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
    public boolean episodeExistsByNovelIdAndEpisodeType(Long novelId, EpisodeType episodeType) {
        Integer result = queryFactory
                .selectOne()
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        episode.episodeType.eq(episodeType),
                        episode.deletedAt.isNull()
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public boolean episodeExistsByNovelIdAndEpisodeType(Long novelId, EpisodeType episodeType, Long id) {
        Integer result = queryFactory
                .selectOne()
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        episode.episodeType.eq(episodeType),
                        episode.id.ne(id),
                        episode.deletedAt.isNull()
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public LocalDateTime findLastEpisodeAtExceptDeletedEpisode(Long novelId, Long id) {
        return queryFactory
                .select(episode.createdAt)
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        episode.id.ne(id),
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
                                episode.episodeType,
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
                        episode.deletedAt.isNull()
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<String> findEpisodeContentByPublicId(String publicId) {
        String result = queryFactory
                .select(episode.content)
                .from(episode)
                .where(
                        episode.publicId.eq(publicId),
                        episode.deletedAt.isNull()
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
                                episode.episodeType,
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
}
