package com.iucyh.novelservice.common.validator.htmlsize;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;

public class SizeWithoutHtmlValidator implements ConstraintValidator<SizeWithoutHtml, String> {

    private int min;
    private int max;

    @Override
    public void initialize(SizeWithoutHtml constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String parsedText = Jsoup.parse(value).wholeText();
        return parsedText.length() >= min && parsedText.length() <= max;
    }
}
