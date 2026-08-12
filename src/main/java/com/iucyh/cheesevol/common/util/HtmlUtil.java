package com.iucyh.cheesevol.common.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * <p>HTML과 관련된 편의 메서드를 제공하는 유틸리티</p>
 */
public class HtmlUtil {

    private HtmlUtil() {}

    /**
     * <p>전달된 HTML 문자열을 정제</p>
     * <p>전달된 문자열이 비어있거나, {@code null}이라면 그 값 그대로 반환</p>
     * @param safelist HTML 정제 시 사용할 white list
     * @param html 정제할 문자열
     * @return 규칙에 맞게 정제된 HTML 문자열
     */
    public static String sanitize(Safelist safelist, String html) {
        if (html == null) {
            return null;
        }

        if (html.isBlank()) {
            return "";
        }

        return Jsoup.clean(html, safelist);
    }

    /**
     * <p>모든 HTML 태그를 제거한 순수 택스트를 반환, new line(\n), 공백은 유지</p>
     * <p>전달된 문자열이 비어있거나, {@code null}이라면 그 값 그대로 반환</p>
     * @param html 변환할 HTML 문자열
     * @return HTML 태그가 모두 제거된 순수 텍스트
     */
    public static String toWholeText(String html) {
        if (html == null) {
            return null;
        }

        if (html.isBlank()) {
            return "";
        }

        return Jsoup.parse(html).wholeText();
    }
}
