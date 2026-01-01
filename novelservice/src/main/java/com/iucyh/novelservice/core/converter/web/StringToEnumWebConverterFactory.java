package com.iucyh.novelservice.core.converter.web;

import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEnumWebConverterFactory implements ConverterFactory<String, ValuedEnum> {

    @Override
    public <T extends ValuedEnum> Converter<String, T> getConverter(Class<T> targetType) {
        if (!targetType.isEnum()) {
            throw new IllegalArgumentException("Target type must be an enum");
        }

        return new StringToEnumWebConverter<>(targetType);
    }

    private static class StringToEnumWebConverter<T extends ValuedEnum> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToEnumWebConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source.isBlank()) return null;

            for (T e : targetType.getEnumConstants()) {
                if (e.getValue().equals(source.trim())) {
                    return e;
                }
            }
            return null;
        }
    }
}
