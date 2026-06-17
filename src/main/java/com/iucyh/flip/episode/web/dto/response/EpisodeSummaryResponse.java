package com.iucyh.flip.episode.web.dto.response;

import com.iucyh.flip.episode.enumtype.EpisodeType;

import java.time.LocalDateTime;

public record EpisodeSummaryResponse(

        String episodeId,
        int episodeNumber,
        EpisodeType episodeType,
        String title,
        String description,
        int viewCount,
        LocalDateTime publishedAt
) {}
