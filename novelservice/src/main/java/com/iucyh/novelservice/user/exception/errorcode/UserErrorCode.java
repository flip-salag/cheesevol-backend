package com.iucyh.novelservice.user.exception.errorcode;

import com.iucyh.novelservice.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-4041", "User not found with this id");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
