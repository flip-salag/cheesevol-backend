package com.iucyh.flip.episode.repository.projection.querydsl;

import com.iucyh.flip.episode.enumtype.EpisodeType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EpisodeSummaryProjection {

    private final String publicId;
    private final EpisodeType episodeType;
    private final String title;
    private final String description;
    private final Integer viewCount;
    private final Integer episodeNumber;
    private final LocalDateTime publishedAt;

    @QueryProjection
    public EpisodeSummaryProjection(
            String publicId, EpisodeType episodeType, String title, String description,
            Integer viewCount, Integer episodeNumber,
            LocalDateTime publishedAt
    ) {
        this.publicId = publicId;
        this.episodeType = episodeType;
        this.title = title;
        this.description = description;
        this.viewCount = viewCount;
        this.episodeNumber = episodeNumber;
        this.publishedAt = publishedAt;
    }
}
