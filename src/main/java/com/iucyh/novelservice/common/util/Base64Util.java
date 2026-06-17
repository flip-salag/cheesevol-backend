package com.iucyh.novelservice.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class Base64Util {

    private final ObjectMapper objectMapper;

    /**
     * <p>전달받은 {@code data}를 Base64 형식으로 인코딩</p>
     * @param data Base64로 변환할 객체
     * @return Base64로 인코딩 된 {@code data}
     * @throws IllegalArgumentException 인코딩 과정 중 객체를 JSON으로 변환하지 못할 경우
     */
    public String encode(Object data) throws IllegalArgumentException {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(data);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to encode to base64", e);
        }
    }

    /**
     * <p>Base64로 인코딩된 값을 전달받은 {@code type}으로 디코딩</p>
     * @param encodedValue 디코딩 할 Base64 문자열
     * @param type 디코딩 될 {@code Class}
     * @return 해당 {@code Class}로 디코딩 된 객체
     * @throws IllegalArgumentException 해당 값이 Base64 형식이 아니거나 JSON 역직렬화가 실패할 때
     */
    public <T> T decode(String encodedValue, Class<T> type) throws IllegalArgumentException {
        try {
            byte[] bytes = Base64.getDecoder().decode(encodedValue);
            return objectMapper.readValue(bytes, type);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to decode from base64", e);
        }
    }
}
