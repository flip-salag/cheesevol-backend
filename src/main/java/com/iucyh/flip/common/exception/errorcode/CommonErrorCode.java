package com.iucyh.flip.common.exception.errorcode;

import com.iucyh.flip.base.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-4041", "Requested resource not found"); // 특정 도메인 리소스를 찾지 못했을 때 사용

    private final HttpStatus status;
    private final String code;
    private final String message;
}
