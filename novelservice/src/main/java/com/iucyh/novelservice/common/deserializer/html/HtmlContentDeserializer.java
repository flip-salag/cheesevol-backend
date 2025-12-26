package com.iucyh.novelservice.common.deserializer.html;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.iucyh.novelservice.common.deserializer.html.registry.SafelistRegistry;
import com.iucyh.novelservice.common.vo.HtmlContent;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * <p>JSON의 HTML 문자열 필드를 내부에서 사용하는 HtmlContent 타입으로 변환</p>
 * <b>@HtmlSanitized 어노테이션을 필드에 붙이지 않거나 Safelist를 조회하기 위한 Key가 유효하지 않다면 {@code IllegalStateException} 예외 발생</b>
 */
@JsonComponent
public class HtmlContentDeserializer extends StdDeserializer<HtmlContent> implements ContextualDeserializer {

    private final SafelistRegistry registry; // Safelist 정책을 조회하기 위한 전용 Registry
    private String safelistKey; // Safelist 정책을 조회하기 위한 Key

    @Autowired
    public HtmlContentDeserializer(SafelistRegistry registry) {
        super(HtmlContent.class);
        this.registry = registry;
    }

    private HtmlContentDeserializer(SafelistRegistry registry, String key) {
        super(HtmlContent.class);
        this.registry = registry;
        this.safelistKey = key;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return new HtmlContentDeserializer(registry);
        }

        // 개발자의 실수와 관련된 예외이므로 Jackson 예외가 아니라 IllegalStateException을 던져 더 명확하게 구분
        HtmlSanitized annotation = property.getAnnotation(HtmlSanitized.class);
        if (annotation == null) { // 필드에 필수 어노테이션이 없다면
            throw new IllegalStateException("@HtmlSanitized is required on property %s".formatted(property.getName()));
        }

        if (!registry.containsKey(annotation.value())) { // 조회에 사용할 Key가 유효하지 않다면
            throw new IllegalStateException("Safelist with key %s is not registered".formatted(annotation.value()));
        }

        return new HtmlContentDeserializer(registry, annotation.value());
    }

    @Override
    public HtmlContent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getValueAsString();

        if (value == null) {
            return null;
        }

        if (safelistKey == null) {
            return ctxt.reportBadDefinition(
                    HtmlContent.class,
                    "HtmlContent cannot be deserialized (property is null)"
            );
        }

        Safelist safelist = registry.getSafelist(safelistKey);
        return HtmlContent.of(value, safelist);
    }
}
