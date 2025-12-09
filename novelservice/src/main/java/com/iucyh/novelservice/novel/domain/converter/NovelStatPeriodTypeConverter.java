package com.iucyh.novelservice.novel.domain.converter;

import com.iucyh.novelservice.novel.enumtype.NovelStatPeriodType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NovelStatPeriodTypeConverter implements AttributeConverter<NovelStatPeriodType, String> {

    @Override
    public String convertToDatabaseColumn(NovelStatPeriodType attribute) {
        return attribute.getValue();
    }

    @Override
    public NovelStatPeriodType convertToEntityAttribute(String dbData) {
        return NovelStatPeriodType.of(dbData);
    }
}
