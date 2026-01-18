package com.iucyh.novelservice.novel.service.codec;

import com.iucyh.novelservice.novel.exception.InvalidNovelCursor;
import com.iucyh.novelservice.common.util.Base64Util;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelBase64CursorCodec implements NovelCursorCodec {

    private final Base64Util base64Util;

    @Override
    public String encode(NovelCursor cursor) {
        return base64Util.encode(cursor);
    }

    @Override
    public <T extends NovelCursor> T decode(String cursor, Class<T> type) throws InvalidNovelCursor {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }

        try {
            return base64Util.decode(cursor, type);
        } catch (IllegalArgumentException e) {
            throw new InvalidNovelCursor(e);
        }
    }
}
