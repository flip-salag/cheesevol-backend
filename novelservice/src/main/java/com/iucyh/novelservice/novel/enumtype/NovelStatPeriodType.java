package com.iucyh.novelservice.novel.enumtype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnum;
import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnumHelper;

public enum NovelStatPeriodType implements ValuedEnum {

    ONE_MONTH("1m");

    private final String value;

    NovelStatPeriodType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    public static NovelStatPeriodType of(String value) {
        return ValuedEnumHelper.fromValue(value, NovelStatPeriodType.class);
    }
}
