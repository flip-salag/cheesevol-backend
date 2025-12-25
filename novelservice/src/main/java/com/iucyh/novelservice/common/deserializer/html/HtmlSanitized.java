package com.iucyh.novelservice.common.deserializer.html;

import com.iucyh.novelservice.common.util.html.HtmlContentType;

import java.lang.annotation.*;

/**
 * <p>정제할 필드의 컨텐트 종류를 명시하기 위한 어노테이션</p>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HtmlSanitized {

    /**
     * 필드의 컨텐트 종류 (회차 본문, 공지사항 본문 등)
     */
    HtmlContentType value();
}
