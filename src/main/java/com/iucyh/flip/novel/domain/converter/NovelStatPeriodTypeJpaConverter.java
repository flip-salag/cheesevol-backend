package com.iucyh.flip.novel.domain.converter;

import com.iucyh.flip.novel.enumtype.NovelStatPeriodType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NovelStatPeriodTypeJpaConverter implements AttributeConverter<NovelStatPeriodType, String> {

    @Override
    public String convertToDatabaseColumn(NovelStatPeriodType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public NovelStatPeriodType convertToEntityAttribute(String dbData) {
        return NovelStatPeriodType.of(dbData);
    }
}
