package com.iucyh.flip.base.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> causes;

    private BusinessException(ErrorCode errorCode, String message, Map<String, Object> causes, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.causes = causes;
    }

    protected BusinessException(ErrorCode errorCode, String message, Map<String, Object> causes) {
        this(errorCode, message, causes, null);
    }

    protected BusinessException(ErrorCode errorCode, String message) {
        this(errorCode, message, null, null);
    }

    protected BusinessException(ErrorCode errorCode, Map<String, Object> causes) {
        this(errorCode, errorCode.getDefaultMessage(), causes, null);
    }

    protected BusinessException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, errorCode.getDefaultMessage(), null, cause);
    }

    protected BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), null, null);
    }
}
