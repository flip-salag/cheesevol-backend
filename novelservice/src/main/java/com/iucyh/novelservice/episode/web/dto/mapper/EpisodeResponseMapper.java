package com.iucyh.novelservice.episode.web.dto.mapper;

import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.common.response.PageResponse;
import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import com.iucyh.novelservice.episode.repository.projection.EpisodeDetail;

import java.util.List;

public class EpisodeResponseMapper {

    private EpisodeResponseMapper() {}

    public static EpisodeSummaryResponse toEpisodeSummaryResponse(Episode episode) {
        return new EpisodeSummaryResponse(
                episode.getId(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getEpisodeNumber(),
                episode.getViewCount(),
                episode.getUpdatedAt(),
                episode.getCreatedAt()
        );
    }

    public static EpisodeSummaryResponse toEpisodeSummaryResponse(EpisodeSimpleQueryDto episode) {
        return new EpisodeSummaryResponse(
                episode.getId(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getEpisodeNumber(),
                episode.getViewCount(),
                episode.getUpdatedAt(),
                episode.getCreatedAt()
        );
    }

    public static EpisodeDetailResponse toEpisodeDetailResponse(Episode episode) {
        return new EpisodeDetailResponse(episode.getContent());
    }

    public static EpisodeDetailResponse toEpisodeDetailResponse(EpisodeDetail episodeDetail) {
        return new EpisodeDetailResponse(episodeDetail.getContent());
    }

    public static PageResponse<EpisodeSummaryResponse> toPagingResponse(List<EpisodeSummaryResponse> episodes, long totalCount, Integer lastEpisodeNumber) {
        return new PageResponse<>(totalCount, lastEpisodeNumber, episodes);
    }
}
