package com.iucyh.flip.base.enumtype.valuedenum;

public class ValuedEnumHelper {

    private ValuedEnumHelper() {}

    /**
     * <p>String 값을 특정 Enum의 값으로 변환하는 공통 메서드<br>
     * {@code ValuedEnum}을 구현한 Enum에 {@code of} 같은 정적 팩토리 메서드를 구현해야 한다면 이 메서드 활용 권장</p>
     * <b>주의 : 각 Enum 별로 변환 로직이 다를 수 있으므로 외부에서 문자열을 Enum으로 변환할때는 이 메서드보다 해당 Enum의 정적 팩토리 메서드를 우선적으로 사용하는 것이 안전</b>
     * @param value 변환하려는 문자열
     * @param enumClass 변환할 Enum 클래스
     * @return <p>Enum의 값 중 주어진 value 문자열과 매칭되는 value 필드를 가진 값이 있다면 해당 값을 반환</p>
     * <p>만약 매칭되는 값이 없거나, value 문자열이 null이거나, value 문자열이 비어있다면(blank) {@code null}을 반환</p>
     */
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
