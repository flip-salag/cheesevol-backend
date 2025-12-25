package com.iucyh.novelservice.common.util.html;

import org.jsoup.Jsoup;

/**
 * <p>HTML과 관련된 편의 메서드를 제공하는 유틸리티</p>
 */
public class HtmlUtil {

    private HtmlUtil() {}

    /**
     * <p>모든 HTML 태그를 제거한 순수 택스트를 반환, new line(\n), 공백은 유지</p>
     * @param html 변환할 HTML 문자열
     * @return HTML 태그가 모두 제거된 순수 텍스트
     */
    public static String toWholeText(String html) {
        return Jsoup.parse(html).wholeText();
    }
}
