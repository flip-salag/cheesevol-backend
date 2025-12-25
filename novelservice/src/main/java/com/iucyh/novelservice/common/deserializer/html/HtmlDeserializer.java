package com.iucyh.novelservice.common.deserializer.html;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.iucyh.novelservice.common.util.html.HtmlContentType;
import com.iucyh.novelservice.common.util.html.HtmlSanitizerUtil;

import java.io.IOException;

/**
 * <p>JSON의 HTML 문자열 필드를 역직렬화 단계에서 규칙에 맞게 정제(sanitize) 및 정규화</p>
 * <p>Request DTO의 필드 중 HTML 정제가 필요한 필드에 사용</p>
 * <b>주의: {@code @HtmlSanitized}를 같이 쓰지 않을 시 원본 문자열 반환</b>
 */
public class HtmlDeserializer extends StdDeserializer<String> implements ContextualDeserializer {

    private final HtmlContentType contentType;

    public HtmlDeserializer() {
        super(String.class);
        this.contentType = null;
    }

    private HtmlDeserializer(HtmlContentType contentType) {
        super(String.class);
        this.contentType = contentType;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return new HtmlDeserializer();
        }

        HtmlSanitized annotation = property.getAnnotation(HtmlSanitized.class);
        if (annotation == null) {
            return new HtmlDeserializer();
        }

        return new HtmlDeserializer(annotation.value());
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getValueAsString();

        if (value == null) {
            return null;
        }

        if (contentType == null) {
            return value;
        }

        return HtmlSanitizerUtil.sanitize(contentType, value);
    }
}
