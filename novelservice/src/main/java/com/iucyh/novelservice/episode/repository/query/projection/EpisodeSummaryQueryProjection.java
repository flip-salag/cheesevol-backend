package com.iucyh.novelservice.episode.repository.query.projection;

import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EpisodeSummaryQueryProjection {

    private final String publicId;
    private final EpisodeType episodeType;
    private final String title;
    private final String description;
    private final Integer viewCount;
    private final Integer episodeNumber;
    private final LocalDateTime createdAt;

    @QueryProjection
    public EpisodeSummaryQueryProjection(
            String publicId, EpisodeType episodeType, String title, String description,
            Integer viewCount, Integer episodeNumber,
            LocalDateTime createdAt
    ) {
        this.publicId = publicId;
        this.episodeType = episodeType;
        this.title = title;
        this.description = description;
        this.viewCount = viewCount;
        this.episodeNumber = episodeNumber;
        this.createdAt = createdAt;
    }
}
