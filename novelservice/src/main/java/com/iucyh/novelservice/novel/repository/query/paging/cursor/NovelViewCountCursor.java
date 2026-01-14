package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDate;

public record NovelViewCountCursor(

        long novelId,
        int totalViewCount,
        LocalDate lastEpisodePublishDate
) implements NovelCursor {}
