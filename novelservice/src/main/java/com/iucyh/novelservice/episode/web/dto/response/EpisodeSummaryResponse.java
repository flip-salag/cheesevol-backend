package com.iucyh.novelservice.episode.web.dto.response;

import java.time.LocalDateTime;

public record EpisodeSummaryResponse(

        String episodeId,
        int episodeNumber,
        String title,
        String description,
        int viewCount,
        LocalDateTime createdAt
) {}
