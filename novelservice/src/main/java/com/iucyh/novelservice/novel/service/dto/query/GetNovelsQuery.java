package com.iucyh.novelservice.novel.service.dto.query;

import com.iucyh.novelservice.novel.enumtype.NovelSortType;

public record GetNovelsQuery(

        NovelSortType sortType,
        String cursor,
        int limit
) {}
