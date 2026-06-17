package com.iucyh.flip.novel.repository.custom.paging.cursor;

import com.iucyh.flip.novel.enumtype.NovelSortType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NovelLastUpdateCursor implements NovelCursor {

    private final NovelSortType sortType;
    private final long novelId;
    private final LocalDate lastEpisodePublishDate;
    private final int totalViewCount;

    public static NovelLastUpdateCursor of(long novelId, LocalDate lastEpisodePublishDate, int totalViewCount) {
        return new NovelLastUpdateCursor(NovelSortType.LAST_UPDATE, novelId, lastEpisodePublishDate, totalViewCount);
    }
}