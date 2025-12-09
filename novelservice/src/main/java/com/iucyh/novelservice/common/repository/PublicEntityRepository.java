package com.iucyh.novelservice.common.repository;

import com.iucyh.novelservice.common.repository.projection.IdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface PublicEntityRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> findByPublicId(String publicId);
    Optional<IdProjection> findIdByPublicId(String publicId);
}
