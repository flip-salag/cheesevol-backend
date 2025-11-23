package com.iucyh.novelservice.novel.repository.query.paging.cursor;

public record NovelPopularCursor(

        long lastAggId,
        int lastAggViewCount
) implements NovelCursor {}
