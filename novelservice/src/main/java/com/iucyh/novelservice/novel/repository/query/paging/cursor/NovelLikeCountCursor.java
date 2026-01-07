package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDateTime;

public record NovelLikeCountCursor(

        long lastNovelId,
        int lastLikeCount,
        LocalDateTime lastPublishedAt
) implements NovelCursor {}
