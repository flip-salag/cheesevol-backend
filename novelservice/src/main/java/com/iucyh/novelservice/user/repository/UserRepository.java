package com.iucyh.novelservice.user.repository;

import com.iucyh.novelservice.common.repository.PublicEntityRepository;
import com.iucyh.novelservice.user.domain.User;

import java.util.Optional;

public interface UserRepository extends PublicEntityRepository<User, Long> {

    Optional<User> findByIdAndDeletedAtIsNull(long id);
}
