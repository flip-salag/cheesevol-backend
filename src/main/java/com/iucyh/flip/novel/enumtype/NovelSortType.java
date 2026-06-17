package com.iucyh.flip.novel.enumtype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnum;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnumHelper;

public enum NovelSortType implements ValuedEnum {

    POPULAR("popular"),
    LAST_UPDATE("last-update"),
    VIEW_COUNT("view-count"),
    LIKE_COUNT("like-count");

    private final String value;
    
    NovelSortType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    public static NovelSortType of(String value) {
        return ValuedEnumHelper.fromValue(value, NovelSortType.class);
    }
}
