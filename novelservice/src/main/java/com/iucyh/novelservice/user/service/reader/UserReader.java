package com.iucyh.novelservice.user.service.reader;

import com.iucyh.novelservice.user.domain.User;
import com.iucyh.novelservice.user.exception.UserNotFound;

public interface UserReader {

    User findUserById(long userId) throws UserNotFound;
}
