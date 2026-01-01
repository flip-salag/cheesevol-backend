package com.iucyh.novelservice.core.validator.htmlnotblank;

import com.iucyh.novelservice.common.vo.HtmlContent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankWithoutHtmlValidator implements ConstraintValidator<NotBlankWithoutHtml, HtmlContent> {

    @Override
    public boolean isValid(HtmlContent value, ConstraintValidatorContext context) {
        return value == null || !value.getTextValue().isBlank();
    }
}
