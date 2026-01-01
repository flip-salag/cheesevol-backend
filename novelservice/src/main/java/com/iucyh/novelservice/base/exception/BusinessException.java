package com.iucyh.novelservice.base.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> causes;

    protected BusinessException(ErrorCode errorCode, String message, Map<String, Object> causes) {
        super(message);
        this.errorCode = errorCode;
        this.causes = causes;
    }

    protected BusinessException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    protected BusinessException(ErrorCode errorCode, Map<String, Object> causes) {
        this(errorCode, errorCode.getDefaultMessage(), causes);
    }

    protected BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), null);
    }
}
