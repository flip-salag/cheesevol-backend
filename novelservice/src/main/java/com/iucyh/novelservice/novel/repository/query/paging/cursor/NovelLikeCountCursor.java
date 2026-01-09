package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDate;

public record NovelLikeCountCursor(

        long lastNovelId,
        int lastLikeCount,
        LocalDate lastEpisodePublishDate
) implements NovelCursor {}
