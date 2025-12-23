package com.iucyh.novelservice.episode.repository;

import com.iucyh.novelservice.common.repository.PublicEntityRepository;
import com.iucyh.novelservice.episode.domain.Episode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EpisodeRepository extends PublicEntityRepository<Episode, Long> {

    /**
     * novelId에 해당하는 소설에 회차가 1개라도 존재하는 지 검사
     */
    boolean existsByNovelIdAndDeletedAtIsNull(Long novelId);

    /**
     * <p>{@code publicId}에 해당하는 회차를 조회하면서 아래 조건들을 동시에 검사</p>
     * <ul>
     *     <li>회차가 삭제되지 않았는지 여부</li>
     *     <li>회차가 속한 소설의 작성자 id가 {@code userId}와 일치하는지 여부</li>
     * </ul>
     */
    @Query("""
    select e
    from Episode e
    join e.novel n
    where
        e.publicId = :publicId
        and e.deletedAt is null
        and n.user.id = :userId
    """)
    Optional<Episode> findByPublicIdWithNovelUser(@Param("publicId") String publicId, @Param("userId") Long userId);

    /**
     * <p>{@code publicId}에 해당하는 회차를 조회하면서 아래 조건들을 동시에 검사</p>
     * <ul>
     *     <li>회차가 삭제되지 않았는지 여부</li>
     *     <li>회차가 속한 소설의 작성자 id가 {@code userId}와 일치하는지 여부</li>
     * </ul>
     * <b>주의: 조회 시 Novel 엔티티도 같이 조회 (fetch join), 이후 로직에서 Novel 엔티티가 필요 없다면 Fetch 가 붙지 않은 메서드 사용 권장</b>
     */
    @Query("""
    select e
    from Episode e
    join fetch e.novel n
    where
        e.publicId = :publicId
        and e.deletedAt is null
        and n.user.id = :userId
    """)
    Optional<Episode> findByPublicIdWithNovelUserFetch(@Param("publicId") String publicId, @Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update Episode e set e.viewCount = e.viewCount + 1 where e.id = :episodeId and e.deletedAt is null")
    void increaseViewCount(@Param("episodeId") Long episodeId);

    /**
     * <p>특정 Novel에 속하는 회차들을 bulk update 로 soft delete 처리</p>
     * <b>이미 삭제된 회차들은 무시</b>
     * @param novelId 삭제할 회차들이 속한 Novel의 pk
     * @param deletedAt 각 Episode에 기록될 삭제 시간, 가급적 현재 시간 혹은 부모 Novel의 삭제 시간 전달 권장
     */
    @Modifying(clearAutomatically = true)
    @Query("""
    update Episode e
    set e.deletedAt = :deletedAt
    where e.novel.id = :novelId and e.deletedAt is null
    """)
    void softDeleteByNovelId(Long novelId, LocalDateTime deletedAt);
}
