package com.iucyh.flip.novel.enumtype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnum;
import com.iucyh.flip.base.enumtype.valuedenum.ValuedEnumHelper;

public enum NovelCategory implements ValuedEnum {

    FANTASY("fantasy"),
    ROMANCE("romance"),
    HORROR("horror"),
    SF("sf"),
    SPORTS("sports"),
    LIFE("life"),
    ETC("etc");

    private final String value;

    NovelCategory(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    public static NovelCategory of(String value) {
        return ValuedEnumHelper.fromValue(value, NovelCategory.class);
    }
}
