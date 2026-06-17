package com.iucyh.novelservice.novel.service.dto.query;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;

public record GetNovelsQuery(

        NovelCategory category,
        NovelSortType sortType,
        String cursor,
        int limit
) {}
