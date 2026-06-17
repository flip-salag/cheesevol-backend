package com.iucyh.flip.common.vo;

import com.iucyh.flip.common.util.HtmlUtil;
import lombok.Getter;
import org.jsoup.safety.Safelist;

/**
 * <p>HTML 문자열을 표현하는 객체</p>
 */
@Getter
public final class HtmlContent {

    /**
     * <p>가공되지 않은 원본 HTML</p>
     */
    private final String originalValue;

    /**
     * <p>허용되지 않는 태그를 정제한 HTML</p>
     */
    private final String sanitizedValue;

    /**
     * <p>모든 HTML 태그를 제거한 순수 텍스트</p>
     */
    private final String textValue;

    private HtmlContent(String originalValue, String sanitizedValue) {
        this.originalValue = originalValue;
        this.sanitizedValue = sanitizedValue;
        this.textValue = HtmlUtil.toWholeText(sanitizedValue);
    }

    public static HtmlContent of(String originalValue, Safelist safelist) {
        if (originalValue == null) {
            throw new IllegalArgumentException("originalValue in HtmlContent cannot be null");
        }

        return new HtmlContent(
                originalValue,
                HtmlUtil.sanitize(safelist, originalValue)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HtmlContent other)) {
            return false;
        }

        return sanitizedValue.equals(other.getSanitizedValue());
    }

    @Override
    public int hashCode() {
        return sanitizedValue.hashCode();
    }
}
