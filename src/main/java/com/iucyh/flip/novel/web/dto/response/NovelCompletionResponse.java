package com.iucyh.flip.novel.web.dto.response;

import java.time.LocalDateTime;

public record NovelCompletionResponse(

        String novelId,
        boolean isCompleted,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {}
