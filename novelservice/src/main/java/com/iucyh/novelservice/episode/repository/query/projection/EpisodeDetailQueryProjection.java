package com.iucyh.novelservice.episode.repository.query.projection;

import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EpisodeDetailQueryProjection {

    private final String episodePublicId;
    private final EpisodeType episodeType;
    private final String episodeTitle;
    private final String episodeDescription;
    private final Integer episodeNumber;
    private final LocalDateTime episodePublishedAt;

    private final Long novelId;
    private final String novelPublicId;
    private final String novelTitle;
    private final Integer novelLikeCount;

    private final String userPublicId;
    private final String userNickname;

    @QueryProjection
    public EpisodeDetailQueryProjection(
            String episodePublicId, EpisodeType episodeType, String episodeTitle, String episodeDescription, Integer episodeNumber, LocalDateTime episodePublishedAt,
            Long novelId, String novelPublicId, String novelTitle, Integer novelLikeCount,
            String userPublicId, String userNickname
    ) {
        this.episodePublicId = episodePublicId;
        this.episodeType = episodeType;
        this.episodeTitle = episodeTitle;
        this.episodeDescription = episodeDescription;
        this.episodeNumber = episodeNumber;
        this.episodePublishedAt = episodePublishedAt;

        this.novelId = novelId;
        this.novelPublicId = novelPublicId;
        this.novelTitle = novelTitle;
        this.novelLikeCount = novelLikeCount;

        this.userPublicId = userPublicId;
        this.userNickname = userNickname;
    }
}
