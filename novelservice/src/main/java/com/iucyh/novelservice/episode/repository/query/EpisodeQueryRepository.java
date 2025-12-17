package com.iucyh.novelservice.episode.repository.query;

import com.iucyh.novelservice.episode.repository.query.condition.EpisodeSearchCondition;
import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EpisodeQueryRepository {

    /**
     * <p>{@code publicId}에 해당하는 회차와 삭제된 회차를 제외한 나머지 회차 중 가장 최신 회차의 생성일 조회</p>
     * @param novelId 조회할 회차들이 속한 소설의 id
     * @param publicId 조회에서 제외할 회차의 public id
     * @return 가장 최신 회차의 생성일, 결과가 없다면 {@code null}
     */
    LocalDateTime findLastEpisodeAtExceptDeletedEpisode(Long novelId, String publicId);

    List<EpisodeSimpleQueryDto> findEpisodesByNovelId(long novelId, EpisodeSearchCondition condition);
}
