package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDate;

public record NovelLastUpdateCursor(

        long novelId,
        LocalDate lastEpisodePublishDate,
        int totalViewCount
) implements NovelCursor {}