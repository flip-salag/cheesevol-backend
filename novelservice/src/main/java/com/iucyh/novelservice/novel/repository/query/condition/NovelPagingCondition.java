package com.iucyh.novelservice.novel.repository.query.condition;

import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;

public record NovelPagingCondition(

        NovelCursor cursor,
        int limit
) {}