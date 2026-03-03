package com.iucyh.novelservice.core.json.deserializer.html;

import java.lang.annotation.*;

/**
 * <p>도메인별 Safelist 정책과 매핑되는 Key를 명시하기 위한 어노테이션</p>
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SanitizedHtml {

    /**
     * 도메인별 Safelist 정책(회차 본문, 공지사항 본문 등)을 조회하기 위한 Key
     */
    String value();
}
