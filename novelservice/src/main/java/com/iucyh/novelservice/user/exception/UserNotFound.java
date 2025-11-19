package com.iucyh.novelservice.user.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.user.exception.errorcode.UserErrorCode;

import java.util.Map;

public class UserNotFound extends ServiceException {

    public UserNotFound(long userId) {
        super(
                UserErrorCode.USER_NOT_FOUND,
                Map.of("userId", userId)
        );
    }
}
