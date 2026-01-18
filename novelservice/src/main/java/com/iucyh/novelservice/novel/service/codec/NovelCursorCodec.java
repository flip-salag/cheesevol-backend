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
     * <p>인코딩된 {@code NovelCursor}를 전달된 {@code type}으로 디코딩</p>
     * @param cursor 디코딩 할 문자열 값
     * @param type 디코딩 될 {@code NovelCursor} 클래스
     * @return 전달된 문자열 값을 기반으로 디코딩 된 {@code NovelCursor}
     * @throws InvalidNovelCursor 전달된 {@code cursor}가 유효한 값이 아닐 때 (Base64 형식이 아니거나 JSON 역직렬화가 불가능할 때)
     */
    <T extends NovelCursor> T decode(String cursor, Class<T> type) throws InvalidNovelCursor;
}
