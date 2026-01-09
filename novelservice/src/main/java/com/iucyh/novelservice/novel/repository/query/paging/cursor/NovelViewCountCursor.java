package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDate;

public record NovelViewCountCursor(

        long lastNovelId,
        int lastTotalViewCount,
        LocalDate lastEpisodePublishDate
) implements NovelCursor {}
