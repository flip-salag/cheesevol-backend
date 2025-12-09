package com.iucyh.novelservice.common.response.api.information;

import com.iucyh.novelservice.common.exception.errorcode.ErrorCode;

public record FailInformation(

        ErrorCode errorCode,
        String message,
        String path
) {}
