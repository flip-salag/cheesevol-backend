package com.iucyh.cheesevol.base.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> causes;

    protected BusinessException(ErrorCode errorCode, Map<String, Object> causes, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.causes = causes;
    }

    protected BusinessException(ErrorCode errorCode, Map<String, Object> causes) {
        this(errorCode, causes, null);
    }

    protected BusinessException(ErrorCode errorCode) {
        this(errorCode, null, null);
    }
}
