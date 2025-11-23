package com.iucyh.novelservice.novel.repository.query.paging.cursor;

public record NovelViewCountCursor(

        long lastNovelId,
        int lastTotalViewCount
) implements NovelCursor {}
