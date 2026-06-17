package com.iucyh.flip.novel.web.dto.response;

import java.time.LocalDateTime;

public record NovelSaveResponse(

        String novelId,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {}
