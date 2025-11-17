package com.iucyh.novelservice.user.service.reader;

import com.iucyh.novelservice.user.domain.User;
import com.iucyh.novelservice.user.exception.UserNotFound;
import com.iucyh.novelservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    @Override
    public User findUserById(long userId) throws UserNotFound {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFound(userId));
    }
}

