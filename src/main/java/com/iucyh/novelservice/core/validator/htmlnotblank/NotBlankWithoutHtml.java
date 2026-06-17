package com.iucyh.novelservice.core.validator.htmlnotblank;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>HTML 태그를 모두 제외한 순수한 텍스트를 기준으로 비어있는지 검증</p>
 * <p>HtmlContent 타입에만 사용 가능</p>
 * <b>주의: 검증 대상 값이 null 인 경우 무시 -> 필요 시 NotNull 등의 null 검증 어노테이션과 같이 쓰기를 권장</b>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotBlankWithoutHtmlValidator.class)
public @interface NotBlankWithoutHtml {

    String message() default "Value cannot be blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
