package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDate;

public record NovelPopularCursor(

        long novelId,
        int periodViewCount,
        LocalDate lastEpisodePublishDate
) implements NovelCursor {}
