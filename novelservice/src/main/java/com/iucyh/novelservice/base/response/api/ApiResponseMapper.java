package com.iucyh.novelservice.base.response.api;

import com.iucyh.novelservice.base.response.api.internal.FailInformation;
import com.iucyh.novelservice.base.exception.BusinessException;

import java.util.Map;

public class ApiResponseMapper {

    private ApiResponseMapper() {}

    public static FailResponse fail(FailInformation info, Map<String, Object> causes) {
        return new FailResponse(info.errorCode(), info.message(), info.path(), causes);
    }

    public static FailResponse fail(FailInformation info) {
        return new FailResponse(info.errorCode(), info.message(), info.path(), null);
    }

    public static FailResponse fail(BusinessException ex, String path) {
        return new FailResponse(ex.getErrorCode(), ex.getMessage(), path, ex.getCauses());
    }

    public static SuccessResponse<Void> success() {
        return new SuccessResponse<>(null);
    }

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<>(data);
    }
}
