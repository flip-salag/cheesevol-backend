package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record NovelLastUpdateCursor(

        long lastNovelId,
        LocalDate lastEpisodePublishDate,
        LocalDateTime lastCreatedAt
) implements NovelCursor {}