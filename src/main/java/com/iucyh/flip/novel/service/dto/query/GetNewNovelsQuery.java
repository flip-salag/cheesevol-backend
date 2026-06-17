package com.iucyh.flip.novel.service.dto.query;

import com.iucyh.flip.novel.enumtype.NovelCategory;
import com.iucyh.flip.novel.enumtype.NovelSortType;

public record GetNewNovelsQuery(

        NovelCategory category,
        NovelSortType sortType,
        String cursor,
        int limit
) {}
