package com.iucyh.novelservice.episode.repository.query;

import com.iucyh.novelservice.episode.repository.query.condition.EpisodePagingCondition;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodeDetailQueryProjection;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodePrevNextQueryProjection;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodeSummaryQueryProjection;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EpisodeQueryRepository {

    /**
     * <p>{@code publicId}에 해당하는 회차와 삭제된 회차를 제외한 나머지 회차 중 가장 최신 회차의 생성일 조회</p>
     * <p>제외할 회차가 없다면 publicId에 null 전달</p>
     * @param novelId 조회할 회차들이 속한 소설의 id
     * @param publicId 조회에서 제외할 회차의 public id (선택)
     * @return 가장 최신 회차의 생성일, 결과가 없다면 {@code null}
     */
    LocalDateTime findLastEpisodeAtExceptDeletedEpisode(Long novelId, String publicId);

    /**
     * <p>현재 회차의 이전 회차 조회</p>
     * @param novelId 조회할 회차들이 속한 소설의 id
     * @param episodeNumber 조회의 기준이 될 회차의 회차 번호
     * @return 기준 회차의 이전 회차, 이전 회차가 존재하지 않는다면 {@code Optional.empty()}
     */
    Optional<EpisodePrevNextQueryProjection> findPrevEpisode(Long novelId, Integer episodeNumber);

    /**
     * <p>현재 회차의 다음 회차 조회</p>
     * @param novelId 조회할 회차들이 속한 소설의 id
     * @param episodeNumber 조회의 기준이 될 회차의 회차 번호
     * @return 기준 회차의 다음 회차, 다음 회차가 존재하지 않는다면 {@code Optional.empty()}
     */
    Optional<EpisodePrevNextQueryProjection> findNextEpisode(Long novelId, Integer episodeNumber);

    /**
     * <p>{@code publicId}에 해당하는 회차의 상세 정보를 조회 (소설, 유저의 일부 정보 포함)</p>
     * <p>회차가 삭제되었다면(soft delete 포함) Optional.empty() 반환</p>
     * @param publicId 조회할 회차의 public id
     * @return 회차의 상세 정보, 조건에 맞는 episode가 없다면 {@code Optional.empty()}
     */
    Optional<EpisodeDetailQueryProjection> findEpisodeDetailByPublicId(String publicId);

    /**
     * <p>{@code publicId}에 해당하는 회차의 본문을 조회</p>
     * <p>회차가 삭제되었다면(soft delete 포함) Optional.empty() 반환</p>
     * @param publicId 조회할 회차의 public id
     * @return 회차의 본문, 조건에 맞는 episode가 없다면 {@code Optional.empty()}
     */
    Optional<String> findEpisodeContentByPublicId(String publicId);

    /**
     * <p>{@code novelPublicId}에 해당하는 소설의 회차 목록을 조회, offset 기반 페이징 사용</p>
     * <p>삭제된 회차는 제외</p>
     * @param novelPublicId 조회할 소설의 public id
     * @param condition 페이징 조건
     * @return 페이지 정보와 회차 목록을 담은 {@code Page<EpisodeSummaryQueryProjection>}
     */
    Page<EpisodeSummaryQueryProjection> findEpisodesByNovelPublicId(String novelPublicId, EpisodePagingCondition condition);
}
