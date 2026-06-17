package com.iucyh.flip.episode.web.dto.response;

import java.time.LocalDateTime;

public record EpisodeSaveResponse(

        String episodeId,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {}
