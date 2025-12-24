package com.iucyh.novelservice.common.util.htmlsanitizer;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.Map;

/**
 * <p>HTML 태그 중 규칙에 맞지 않는 태그, 악성 스크립트 등을 정제하는 유틸리티</p>
 */
public class HtmlSanitizerUtil {

    private static final Map<HtmlContentType, Safelist> safelistMap = Map.of(
            HtmlContentType.EPISODE_CONTENT,
            new Safelist()
                    .addTags("p", "b", "strong", "i", "em", "u", "br", "span")
    );

    private HtmlSanitizerUtil() {}

    /**
     * <p>전달된 HTML 문자열을 내용의 종류에 따라 적절히 정제</p>
     * @param type 정제할 문자열 내용의 종류 (회차 본문, 공지사항 본문 등)
     * @param html 정제할 문자열
     * @return 규칙에 맞게 정제된 HTML 문자열
     */
    public static String sanitize(HtmlContentType type, String html) {
        if (html == null || html.isBlank()) {
            return "";
        }

        Safelist safelist = safelistMap.get(type);
        return Jsoup.clean(html, safelist);
    }
}
