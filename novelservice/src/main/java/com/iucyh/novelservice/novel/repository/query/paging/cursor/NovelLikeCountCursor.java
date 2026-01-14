package com.iucyh.novelservice.novel.repository.query.paging.cursor;

public record NovelLikeCountCursor(

        long novelId,
        int likeCount,
        int totalViewCount
) implements NovelCursor {}
