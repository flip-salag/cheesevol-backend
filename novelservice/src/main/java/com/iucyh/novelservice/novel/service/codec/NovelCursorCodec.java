package com.iucyh.novelservice.novel.service.codec;

import com.iucyh.novelservice.novel.exception.InvalidNovelCursor;
import com.iucyh.novelservice.novel.repository.query.paging.cursor.NovelCursor;

public interface NovelCursorCodec {

    /**
     * <p>{@code NovelCursor} 타입의 객체를 등록된 Codec에 따라 Base64, JWT 등의 형식으로 적절히 인코딩</p>
     * @param cursor 인코딩 할 {@code NovelCursor}
     * @return 인코딩 된 {@code NovelCursor}
     */
    String encode(NovelCursor cursor);

    /**
     * <p>인코딩된 {@code NovelCursor}를 전달된 type으로 디코딩</p>
     * @param cursor 디코딩 할 문자열 값
     * @param type 디코딩 될 {@code NovelCursor} Class
     * @return 전달된 문자열 값을 기반으로 디코딩 된 {@code NovelCursor}
     * @throws InvalidNovelCursor 디코딩 실패 시(해당 type으로 디코딩이 불가능하거나, 기타 여러 이유로 디코딩이 실패할 때)
     */
    <T extends NovelCursor> NovelCursor decode(String cursor, Class<T> type) throws InvalidNovelCursor;
}
