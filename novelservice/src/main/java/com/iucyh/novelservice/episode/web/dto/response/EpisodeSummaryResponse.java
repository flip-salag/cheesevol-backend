package com.iucyh.novelservice.episode.web.dto.response;

import com.iucyh.novelservice.episode.enumtype.EpisodeType;

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
