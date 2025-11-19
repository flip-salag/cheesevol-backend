package com.iucyh.novelservice.novel.repository;

import com.iucyh.novelservice.common.repository.PublicEntityRepository;
import com.iucyh.novelservice.novel.domain.Novel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NovelRepository extends PublicEntityRepository<Novel, Long> {

    /**
     * userId + publicId 에 해당하는 소설을 조회
     */
    Optional<Novel> findByUserIdAndPublicIdAndDeletedAtIsNull(Long userId, String publicId);

    /**
     * <p>특정 작가의 소설 중 전달된 title과 중복되는 제목이 존재하는 지 검사</p>
     * <b>Soft Delete 된 소설을 포함하여 검사</b>
     */
    boolean existsByTitleAndUserId(String title, Long userId);

    /**
     * <p>특정 작가의 소설 중 전달된 publicId 에 해당하는 소설을 제외하고
     * 나머지 소설들 중에서 전달된 title과 중복되는 제목이 존재하는 지 검사</p>
     * <b>Soft Delete 된 소설을 포함하여 검사</b>
     */
    boolean existsByTitleAndUserIdAndPublicIdNot(String title, Long userId, String publicId);

    long countByDeletedAtIsNull();

    @Modifying(clearAutomatically = true)
    @Query("update Novel n set n.totalViewCount = n.totalViewCount + 1 where n.id = :novelId and n.deletedAt is null")
    void increaseTotalViewCount(@Param("novelId") Long novelId);
}
