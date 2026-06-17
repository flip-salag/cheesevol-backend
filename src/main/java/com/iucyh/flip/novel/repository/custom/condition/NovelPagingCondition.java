package com.iucyh.flip.novel.repository.custom.condition;

import com.iucyh.flip.novel.repository.custom.paging.cursor.NovelCursor;

public record NovelPagingCondition(

        NovelCursor cursor,
        int limit
) {}