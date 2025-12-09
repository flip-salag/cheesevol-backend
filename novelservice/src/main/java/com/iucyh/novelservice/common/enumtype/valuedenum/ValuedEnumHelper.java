package com.iucyh.novelservice.common.enumtype.valuedenum;

public class ValuedEnumHelper {

    private ValuedEnumHelper() {}

    public static <T extends Enum<?> & ValuedEnum> T fromValue(String value, Class<T> enumClass) {
        if (value == null || value.isBlank()) return null;

        for (T e : enumClass.getEnumConstants()) {
            if (e.getValue().equals(value.trim())) {
                return e;
            }
        }
        return null;
    }
}
