package com.iucyh.novelservice.core.json.deserializer.html;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.iucyh.novelservice.common.vo.HtmlContent;
import com.iucyh.novelservice.core.json.deserializer.html.registry.SafelistRegistry;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * <p>JSON의 HTML 문자열 필드를 내부에서 사용하는 HtmlContent 타입으로 변환</p>
 * <b>@SanitizedHtml 어노테이션을 찾을 수 없거나 Safelist를 조회하기 위한 Key가 유효하지 않다면 실행 중 예외 발생 가능</b>
 */
@JsonComponent
public class HtmlContentDeserializer extends StdDeserializer<HtmlContent> implements ContextualDeserializer {

    private final SafelistRegistry registry; // Safelist 정책을 조회하기 위한 전용 Registry
    private final String safelistKey; // Safelist 정책을 조회하기 위한 Key

    @Autowired
    public HtmlContentDeserializer(SafelistRegistry registry) {
        this(registry, null);
    }

    private HtmlContentDeserializer(SafelistRegistry registry, String safelistKey) {
        super(HtmlContent.class);
        this.registry = registry;
        this.safelistKey = safelistKey;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return this;
        }

        SanitizedHtml annotation = getSanitizedHtmlAnnotation(ctxt, property);
        if (!registry.containsKey(annotation.value())) { // 조회에 사용할 Key가 유효하지 않다면
            return ctxt.reportBadDefinition(
                    HtmlContent.class,
                    "Safelist with key %s is not registered".formatted(annotation.value())
            );
        }

        return new HtmlContentDeserializer(registry, annotation.value());
    }

    @Override
    public HtmlContent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (safelistKey == null) {
            return ctxt.reportBadDefinition(
                    HtmlContent.class,
                    "HtmlContent cannot be deserialized. (safelistKey is null)"
            );
        }

        JsonToken token = p.currentToken();
        if (token == JsonToken.VALUE_NULL) { // 필드의 값이 null이라면 그대로 null 반환
            return null;
        }

        if (token != JsonToken.VALUE_STRING) {
            return ctxt.reportInputMismatch(
                    HtmlContent.class,
                    "HtmlContent only supports String type, current type: %s", token
            );
        }

        String value = p.getValueAsString();
        Safelist safelist = registry.getSafelist(safelistKey);
        return HtmlContent.of(value, safelist);
    }

    /**
     * <p>주어진 {@code property}를 이용해 {@code SanitizedHtml} 어노테이션 추출</p>
     * @param ctxt 현재 메서드를 호출한 곳에서 사용하고 있는 {@code DeserializationContext}
     * @param property 어노테이션을 추출할 대상
     * @return 추출한 {@code SanitizedHtml} 어노테이션
     * @throws JsonMappingException 필드 혹은 필드의 상위(필드가 소속된 클래스 등)에 어노테이션이 없을 때
     */
    private SanitizedHtml getSanitizedHtmlAnnotation(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        SanitizedHtml annotation = property.getAnnotation(SanitizedHtml.class);
        if (annotation == null) { // 필드에 필수 어노테이션이 없다면
            annotation = property.getContextAnnotation(SanitizedHtml.class);
            if (annotation == null) { // 필수 어노테이션이 상위(필드가 소속된 클래스 등)에도 없다면 예외 발생
                AnnotatedMember member = property.getMember();
                String exceptionMessage = "@SanitizedHtml is required on property '%s' in %s"
                        .formatted(property.getName(), member != null ? member.getDeclaringClass().getName() : "unknown");

                return ctxt.reportBadDefinition(HtmlContent.class, exceptionMessage);
            }
        }

        return annotation;
    }
}
