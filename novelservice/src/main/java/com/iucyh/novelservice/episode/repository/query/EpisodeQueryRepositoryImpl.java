package com.iucyh.novelservice.episode.repository.query;

import com.iucyh.novelservice.episode.repository.query.condition.EpisodeSearchCondition;
import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.episode.repository.query.dto.QEpisodeSimpleQueryDto;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodeDetailQueryProjection;
import com.iucyh.novelservice.episode.repository.query.projection.QEpisodeDetailQueryProjection;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
    public Optional<EpisodeDetailQueryProjection> findEpisodeDetailByPublicId(String publicId) {
        EpisodeDetailQueryProjection result = queryFactory
                .select(
                        new QEpisodeDetailQueryProjection(
                                episode.publicId,
                                episode.title,
                                episode.description,
                                episode.episodeNumber,
                                episode.createdAt,

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
                        episode.deletedAt.isNull(),
                        novel.deletedAt.isNull(),
                        user.deletedAt.isNull()
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<EpisodeSimpleQueryDto> findEpisodesByNovelId(long novelId, EpisodeSearchCondition condition) {
        JPAQuery<EpisodeSimpleQueryDto> query = queryFactory
                .select(new QEpisodeSimpleQueryDto(
                        episode.id,
                        episode.title,
                        episode.description,
                        episode.episodeNumber,
                        episode.viewCount,
                        episode.updatedAt,
                        episode.createdAt
                ))
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        episode.deletedAt.isNull()
                )
                .orderBy(episode.episodeNumber.desc())
                .limit(condition.limit());
        if (isNotFirstPage(condition.lastEpisodeNumber())) {
            Integer lastEpisodeNumber = condition.lastEpisodeNumber();
            query.where(episode.episodeNumber.lt(lastEpisodeNumber));
        }

        return query.fetch();
    }

    private boolean isNotFirstPage(Integer lastEpisodeNumber) {
        return lastEpisodeNumber != null;
    }
}
