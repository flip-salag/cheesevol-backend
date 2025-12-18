package com.iucyh.novelservice.episode.repository.query.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EpisodeDetailQueryProjection {

    private final String episodePublicId;
    private final String episodeTitle;
    private final String episodeDescription;
    private final Integer episodeNumber;
    private final LocalDateTime episodeCreatedAt;

    private final String novelPublicId;
    private final String novelTitle;
    private final Integer novelLikeCount;

    private final String userPublicId;
    private final String userNickname;

    @QueryProjection
    public EpisodeDetailQueryProjection(
            String episodePublicId, String episodeTitle, String episodeDescription, Integer episodeNumber, LocalDateTime episodeCreatedAt,
            String novelPublicId, String novelTitle, Integer novelLikeCount,
            String userPublicId, String userNickname
    ) {
        this.episodePublicId = episodePublicId;
        this.episodeTitle = episodeTitle;
        this.episodeDescription = episodeDescription;
        this.episodeNumber = episodeNumber;
        this.episodeCreatedAt = episodeCreatedAt;

        this.novelPublicId = novelPublicId;
        this.novelTitle = novelTitle;
        this.novelLikeCount = novelLikeCount;

        this.userPublicId = userPublicId;
        this.userNickname = userNickname;
    }
}
