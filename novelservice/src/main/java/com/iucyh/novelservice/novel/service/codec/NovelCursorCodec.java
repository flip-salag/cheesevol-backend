package com.iucyh.novelservice.novel.service.codec;

import com.iucyh.novelservice.novel.exception.InvalidNovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;

public interface NovelCursorCodec {

    String encode(NovelCursor cursor);
    <T extends NovelCursor> NovelCursor decode(String cursor, Class<T> type) throws InvalidNovelCursor;
}
