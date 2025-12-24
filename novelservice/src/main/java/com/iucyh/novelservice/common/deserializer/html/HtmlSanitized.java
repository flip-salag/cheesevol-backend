package com.iucyh.novelservice.common.deserializer.html;

import com.iucyh.novelservice.common.util.htmlsanitizer.HtmlContentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>정제할 필드의 컨텐트 종류를 명시하기 위한 어노테이션</p>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface HtmlSanitized {

    /**
     * 필드의 컨텐트 종류 (회차 본문, 공지사항 본문 등)
     */
    HtmlContentType value();
}
