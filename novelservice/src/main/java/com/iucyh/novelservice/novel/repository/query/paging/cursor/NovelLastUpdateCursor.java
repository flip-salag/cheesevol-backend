package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDateTime;

public record NovelLastUpdateCursor(

        long lastNovelId,
        LocalDateTime lastPublishedAt,
        LocalDateTime lastCreatedAt
) implements NovelCursor {}