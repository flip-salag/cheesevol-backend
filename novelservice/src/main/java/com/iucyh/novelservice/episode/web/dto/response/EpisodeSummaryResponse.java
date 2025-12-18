package com.iucyh.novelservice.episode.web.dto.response;

import java.time.LocalDateTime;

public record EpisodeSummaryResponse(

        String episodeId,
        String title,
        String description,
        int viewCount,
        int episodeNumber,
        LocalDateTime createdAt
) {}
