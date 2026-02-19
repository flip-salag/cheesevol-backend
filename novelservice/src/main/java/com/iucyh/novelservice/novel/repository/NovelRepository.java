package com.iucyh.novelservice.novel.repository;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.custom.NovelCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long>, NovelCustomRepository {

    /**
     * <p>전달된 publicId에 해당하는 소설을 조회하면서 작성자의 id가 전달된 userId인지 동시에 검사</p>
     * @param userId 작성자의 user id pk
     * @param publicId 조회할 소설의 public id
     * @return 조회된 Novel 엔티티, 조건에 맞는 소설이 존재하지 않거나 삭제되었다면(soft delete 포함) {@code Optional.empty()} 반환
     */
    Optional<Novel> findByUserIdAndPublicIdAndDeletedAtIsNull(Long userId, String publicId);

    /**
     * <p>{@code publicId}에 해당하는 소설 조회</p>
     * <b>조회 시 User 엔티티도 같이 조회(fetch join)</b>
     * @param publicId 조회할 소설의 public id
     * @return 조회된 Novel 엔티티, 조건에 맞는 소설이 존재하지 않거나 삭제되었다면(soft delete 포함) {@code Optional.empty()} 반환
     */
    @Query("""
    select n
    from Novel n
    join fetch n.user u
    where
        n.publicId = :publicId
        and n.deletedAt is null
    """)
    Optional<Novel> findByPublicIdFetch(@Param("publicId") String publicId);

    /**
     * <p>{@code id}에 해당하는 소설의 common_episode_count 값을 1만큼 증가</p>
     * @param id 증가시킬 소설의 pk
     */
    @Modifying(clearAutomatically = true)
    @Query("""
    update Novel n
    set n.commonEpisodeCount = n.commonEpisodeCount + 1
    where n.id = :id and n.deletedAt is null
    """)
    void increaseCommonEpisodeCount(@Param("id") Long id);

    /**
     * <p>{@code id}에 해당하는 소설의 common_episode_count 값을 1만큼 감소</p>
     * @param id 감소시킬 소설의 pk
     */
    @Modifying(clearAutomatically = true)
    @Query("""
    update Novel n
    set n.commonEpisodeCount = n.commonEpisodeCount - 1
    where n.id = :id and n.commonEpisodeCount > 0 and n.deletedAt is null
    """)
    void decreaseCommonEpisodeCount(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Novel n set n.totalViewCount = n.totalViewCount + 1 where n.id = :novelId and n.deletedAt is null")
    void increaseTotalViewCount(@Param("novelId") Long novelId);
}
