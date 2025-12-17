package com.iucyh.novelservice.episode.repository;

import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.episode.repository.projection.EpisodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    /**
     * novelId에 해당하는 소설에 회차가 1개라도 존재하는 지 검사
     */
    boolean existsByNovelIdAndDeletedAtIsNull(Long novelId);

    /**
     * <p>{@code publicId}에 해당하는 회차를 조회하면서 아래 조건들을 동시에 검사</p>
     * <ul>
     *     <li>회차가 삭제되지 않았는지 여부</li>
     *     <li>회차가 속한 소설이 삭제되지 않았는지 여부</li>
     *     <li>회차가 속한 소설의 작성자 id가 {@code userId}와 일치하는지 여부</li>
     * </ul>
     */
    @Query("""
    select e
    from Episode e
    join e.novel n
    where
        e.publicId = :publicId and e.deletedAt is null
        and n.user.id = :userId and n.deletedAt is null
    """)
    Optional<Episode> findByPublicIdWithNovelUser(@Param("publicId") String publicId, @Param("userId") Long userId);

    /**
     * <p>{@code publicId}에 해당하는 회차를 조회하면서 아래 조건들을 동시에 검사</p>
     * <ul>
     *     <li>회차가 삭제되지 않았는지 여부</li>
     *     <li>회차가 속한 소설이 삭제되지 않았는지 여부</li>
     *     <li>회차가 속한 소설의 작성자 id가 {@code userId}와 일치하는지 여부</li>
     * </ul>
     * <b>주의: 조회 시 Novel 엔티티도 같이 조회 (fetch join), 이후 로직에서 Novel 엔티티가 필요 없다면 Fetch 가 붙지 않은 메서드 사용 권장</b>
     */
    @Query("""
    select e
    from Episode e
    join fetch e.novel n
    where
        e.publicId = :publicId and e.deletedAt is null
        and n.user.id = :userId and n.deletedAt is null
    """)
    Optional<Episode> findByPublicIdWithNovelUserFetch(@Param("publicId") String publicId, @Param("userId") Long userId);

    @Query("select count(e) from Episode e where e.novel.id = :novelId and e.deletedAt is null")
    int countByNovelId(@Param("novelId") Long novelId);

    @Query("select e.id as id, e.content as content from Episode e where e.novel.id = :novelId and e.episodeNumber = :episodeNumber and e.deletedAt is null")
    Optional<EpisodeDetail> findEpisodeDetail(@Param("novelId") Long novelId, @Param("episodeNumber") Integer episodeNumber);

    @Modifying(clearAutomatically = true)
    @Query("update Episode e set e.viewCount = e.viewCount + 1 where e.id = :episodeId and e.deletedAt is null")
    void increaseViewCount(@Param("episodeId") Long episodeId);
}
