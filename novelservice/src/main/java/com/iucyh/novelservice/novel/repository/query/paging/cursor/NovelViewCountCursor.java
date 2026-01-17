package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NovelViewCountCursor implements NovelCursor {

    private final NovelSortType sortType;
    private final long novelId;
    private final int totalViewCount;
    private final LocalDate lastEpisodePublishDate;

    public static NovelViewCountCursor of(long novelId, int totalViewCount, LocalDate lastEpisodePublishDate) {
        return new NovelViewCountCursor(NovelSortType.VIEW_COUNT, novelId, totalViewCount, lastEpisodePublishDate);
    }
}
