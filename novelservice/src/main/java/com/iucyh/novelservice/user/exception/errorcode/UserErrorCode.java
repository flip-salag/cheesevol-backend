package com.iucyh.novelservice.user.exception.errorcode;

import com.iucyh.novelservice.base.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    ; // 아무것도 없는 Enum 에러 방지 (임시)

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
