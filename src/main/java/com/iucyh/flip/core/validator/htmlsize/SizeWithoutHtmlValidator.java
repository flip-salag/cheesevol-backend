package com.iucyh.flip.core.validator.htmlsize;

import com.iucyh.flip.common.vo.HtmlContent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SizeWithoutHtmlValidator implements ConstraintValidator<SizeWithoutHtml, HtmlContent> {

    private int min;
    private int max;

    @Override
    public void initialize(SizeWithoutHtml constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(HtmlContent value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String text = value.getTextValue();
        return text.length() >= min && text.length() <= max;
    }
}
