package com.iucyh.novelservice.novel.repository.query.paging.cursor;

import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NovelPopularCursor implements NovelCursor {

    private final NovelSortType sortType;
    private final long novelId;
    private final int periodViewCount;
    private final LocalDate lastEpisodePublishDate;

    public static NovelPopularCursor of(long novelId, int periodViewCount, LocalDate lastEpisodePublishDate) {
        return new NovelPopularCursor(NovelSortType.POPULAR, novelId, periodViewCount, lastEpisodePublishDate);
    }
}
