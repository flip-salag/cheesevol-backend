package com.iucyh.novelservice.episode.repository.custom;

import com.iucyh.novelservice.episode.enumtype.EpisodeSortType;
import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.repository.custom.condition.EpisodePagingCondition;
import com.iucyh.novelservice.episode.repository.projection.querydsl.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
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
public class EpisodeCustomRepositoryImpl implements EpisodeCustomRepository {

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
    public LocalDateTime findLastEpisodePublishedAt(Long novelId, Long id) {
        return queryFactory
                .select(episode.publishedAt)
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
    public Optional<EpisodePrevNextProjection> findPrevEpisode(Long novelId, Integer episodeNumber) {
        EpisodePrevNextProjection result = queryFactory
                .select(Projections.constructor(
                        EpisodePrevNextProjection.class,
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
    public Optional<EpisodePrevNextProjection> findNextEpisode(Long novelId, Integer episodeNumber) {
        EpisodePrevNextProjection result = queryFactory
                .select(Projections.constructor(
                        EpisodePrevNextProjection.class,
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
    public Optional<EpisodeDetailProjection> findEpisodeDetailByPublicId(String publicId) {
        EpisodeDetailProjection result = queryFactory
                .select(
                        new QEpisodeDetailProjection(
                                episode.publicId,
                                episode.episodeType,
                                episode.title,
                                episode.description,
                                episode.episodeNumber,
                                episode.publishedAt,

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
    public Page<EpisodeSummaryProjection> findEpisodesByNovelPublicId(String novelPublicId, EpisodePagingCondition condition) {
        Pageable pageable = condition.pageable();
        List<EpisodeSummaryProjection> content = queryFactory
                .select(
                        new QEpisodeSummaryProjection(
                                episode.publicId,
                                episode.episodeType,
                                episode.title,
                                episode.description,
                                episode.viewCount,
                                episode.episodeNumber,
                                episode.publishedAt
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
