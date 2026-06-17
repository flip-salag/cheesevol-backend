package com.iucyh.flip.base.response.api;

import com.iucyh.flip.base.exception.BusinessException;
import com.iucyh.flip.base.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.Map;

public record FailResponse(

        Boolean isSuccess,
        LocalDateTime timestamp,
        String code,
        String message,
        String path,
        Map<String, Object> causes
) {
    private FailResponse(ErrorCode errorCode, String path, Map<String, Object> causes) {
        this(false, LocalDateTime.now(), errorCode.getCode(), errorCode.getMessage(), path, causes);
    }

    public static FailResponse of(ErrorCode errorCode, String path, Map<String, Object> causes) {
        return new FailResponse(errorCode, path, causes);
    }

    public static FailResponse of(ErrorCode errorCode, String path) {
        return new FailResponse(errorCode, path, null);
    }

    public static FailResponse from(BusinessException ex, String path) {
        return new FailResponse(ex.getErrorCode(), path, ex.getCauses());
    }
}

