package com.iucyh.novelservice.novel.repository.query.paging.cursor;

public record NovelPopularCursor(

        long lastNovelId,
        int lastPeriodViewCount,
        int lastTotalViewCount
) implements NovelCursor {}
