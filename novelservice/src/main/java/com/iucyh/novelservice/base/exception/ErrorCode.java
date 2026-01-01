package com.iucyh.novelservice.base.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus getStatus();
    String getCode();
    String getDefaultMessage();
}
