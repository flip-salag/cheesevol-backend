package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDateTime;

public record NovelViewCountCursor(

        long lastNovelId,
        int lastTotalViewCount,
        LocalDateTime lastEpisodeAt
) implements NovelCursor {}
