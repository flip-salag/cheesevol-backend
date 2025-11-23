package com.iucyh.novelservice.novel.repository.query.paging.cursor;

public record NovelLikeCountCursor(

        long lastNovelId,
        int lastLikeCount
) implements NovelCursor {}
