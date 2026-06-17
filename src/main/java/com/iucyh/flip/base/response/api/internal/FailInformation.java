package com.iucyh.flip.base.response.api.internal;

import com.iucyh.flip.base.exception.ErrorCode;

public record FailInformation(

        ErrorCode errorCode,
        String message,
        String path
) {}
