package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record NovelLastUpdateCursor(

        long novelId,
        LocalDate lastEpisodePublishDate,
        int totalViewCount
) implements NovelCursor {}