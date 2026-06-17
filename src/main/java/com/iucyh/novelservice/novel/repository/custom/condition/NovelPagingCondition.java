package com.iucyh.novelservice.novel.repository.custom.condition;

import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelCursor;

public record NovelPagingCondition(

        NovelCursor cursor,
        int limit
) {}