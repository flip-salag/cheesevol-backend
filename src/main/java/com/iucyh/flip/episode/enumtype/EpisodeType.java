package com.iucyh.flip.episode.enumtype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnum;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnumHelper;

public enum EpisodeType implements ValuedEnum {

    COMMON("common"),
    PROLOGUE("prologue");

    private final String value;

    EpisodeType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    public static EpisodeType of(String value) {
        return ValuedEnumHelper.fromValue(value, EpisodeType.class);
    }
}
