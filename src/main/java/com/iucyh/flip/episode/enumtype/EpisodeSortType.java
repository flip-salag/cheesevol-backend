package com.iucyh.flip.episode.enumtype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnum;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnumHelper;

public enum EpisodeSortType implements ValuedEnum {

    DESC("desc"),
    ASC("asc");

    private final String value;

    EpisodeSortType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    public static EpisodeSortType of(String value) {
        return ValuedEnumHelper.fromValue(value, EpisodeSortType.class);
    }
}
