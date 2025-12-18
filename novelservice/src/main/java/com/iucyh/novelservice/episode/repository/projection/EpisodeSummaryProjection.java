package com.iucyh.novelservice.episode.repository.projection;

import java.time.LocalDateTime;

public record EpisodeSummaryProjection(

        String publicId,
        String title,
        String description,
        Integer viewCount,
        Integer episodeNumber,
        LocalDateTime createdAt
) {}
