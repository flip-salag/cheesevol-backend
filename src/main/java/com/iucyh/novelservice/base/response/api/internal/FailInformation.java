package com.iucyh.novelservice.base.response.api.internal;

import com.iucyh.novelservice.base.exception.ErrorCode;

public record FailInformation(

        ErrorCode errorCode,
        String message,
        String path
) {}
