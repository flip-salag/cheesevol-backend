package com.iucyh.novelservice.novel.repository.custom.paging.cursor;

import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NovelLikeCountCursor implements NovelCursor {

    private final NovelSortType sortType;
    private final long novelId;
    private final int likeCount;
    private final int totalViewCount;

    public static NovelLikeCountCursor of(long novelId, int likeCount, int totalViewCount) {
        return new NovelLikeCountCursor(NovelSortType.LIKE_COUNT, novelId, likeCount, totalViewCount);
    }
}
