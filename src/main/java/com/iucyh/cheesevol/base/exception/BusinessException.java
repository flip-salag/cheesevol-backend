package com.iucyh.cheesevol.base.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>모든 비즈니스 예외 클래스들이 반드시 상속해야 하는 기반 클래스</p>
 * <p>이 클래스를 상속받는 자식 클래스는 생성자에서 부모의 생성자를 호출해 예외에 맞는 {@link ErrorCode}를 반드시 설정해야 함</p>
 * <p>예외의 원인을 추가해야 하는 경우 {@link BusinessException#addToCauses(String, Object)} 메서드 사용</p>
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    @Getter(lombok.AccessLevel.NONE)
    private Map<String, Object> causes;

    protected BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    protected BusinessException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    /**
     * 비즈니스 예외의 원인이 된 값을 추가
     * <p>해당 메서드를 여러번 호출해 원하는 만큼 원인들을 추가할 수 있음</p>
     * @param key 값을 설명하는 문자열 키 (e.g. 원인이 된 값이 회원의 아이디라면 "id")
     * @param value 원인이 된 값
     * @throws NullPointerException {@code key} 또는 {@code value}가 {@code null}일 때
     */
    protected void addToCauses(String key, Object value) {
        Objects.requireNonNull(key, "addToCauses key must not be null");
        Objects.requireNonNull(value, () -> "addToCauses value for key [" + key + "] must not be null");

        if (causes == null) {
            causes = new LinkedHashMap<>();
        }
        causes.put(key, value);
    }

    /**
     * @return {@code causes}가 {@code null}이거나 비어있다면 빈 Map (불변), 아니라면 수정 불가능한 Map으로 감싼 {@code causes}
     */
    public Map<String, Object> getCauses() {
        if (causes == null || causes.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(causes);
    }

    /**
     * @return 설정된 {@link ErrorCode}의 {@code message}
     */
    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
