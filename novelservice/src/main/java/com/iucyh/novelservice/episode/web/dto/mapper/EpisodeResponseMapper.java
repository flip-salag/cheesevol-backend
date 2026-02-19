package com.iucyh.novelservice.episode.web.dto.mapper;

import com.iucyh.novelservice.common.response.PageWithOffsetResponse;
import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.episode.repository.projection.querydsl.EpisodeDetailProjection;
import com.iucyh.novelservice.episode.repository.projection.querydsl.EpisodePrevNextProjection;
import com.iucyh.novelservice.episode.repository.projection.querydsl.EpisodeSummaryProjection;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeContentResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSaveResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public class EpisodeResponseMapper {

    private EpisodeResponseMapper() {}

    public static EpisodeSaveResponse toEpisodeSaveResponse(Episode episode) {
        return new EpisodeSaveResponse(
                episode.getPublicId(),
                episode.getUpdatedAt(),
                episode.getCreatedAt()
        );
    }

    public static EpisodeSummaryResponse toEpisodeSummaryResponse(EpisodeSummaryProjection episode) {
        return new EpisodeSummaryResponse(
                episode.getPublicId(),
                episode.getEpisodeNumber(),
                episode.getEpisodeType(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getViewCount(),
                episode.getPublishedAt()
        );
    }

    public static EpisodeDetailResponse toEpisodeDetailResponse(EpisodeDetailProjection episodeDetail, EpisodePrevNextProjection prev, EpisodePrevNextProjection next) {
        UserBasicInfo author = new UserBasicInfo(episodeDetail.getUserPublicId(), episodeDetail.getUserNickname());
        EpisodeDetailResponse.NovelInfo novelInfo = new EpisodeDetailResponse.NovelInfo(
                episodeDetail.getNovelPublicId(),
                author,
                episodeDetail.getNovelTitle(),
                episodeDetail.getNovelLikeCount()
        );
        EpisodeDetailResponse.EpisodePrevNext episodePrevNext = getEpisodePrevNext(prev, next);

        return new EpisodeDetailResponse(
                episodeDetail.getEpisodePublicId(),
                episodeDetail.getEpisodeNumber(),
                episodeDetail.getEpisodeType(),
                episodeDetail.getEpisodeTitle(),
                episodeDetail.getEpisodeDescription(),
                episodeDetail.getEpisodePublishedAt(),
                novelInfo,
                episodePrevNext
        );
    }

    public static EpisodeContentResponse toEpisodeContentResponse(String episodePublicId, String content) {
        return new EpisodeContentResponse(episodePublicId, content);
    }

    public static PageWithOffsetResponse<EpisodeSummaryResponse> toPageResponse(Page<EpisodeSummaryProjection> page) {
        List<EpisodeSummaryResponse> episodes = page.getContent().stream()
                .map(EpisodeResponseMapper::toEpisodeSummaryResponse)
                .toList();
        return new PageWithOffsetResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                episodes
        );
    }

    private static EpisodeDetailResponse.EpisodePrevNext getEpisodePrevNext(EpisodePrevNextProjection prev, EpisodePrevNextProjection next) {
        EpisodeDetailResponse.EpisodePrevNextItem prevEpisode = null;
        EpisodeDetailResponse.EpisodePrevNextItem nextEpisode = null;

        if (prev != null) {
            prevEpisode = new EpisodeDetailResponse.EpisodePrevNextItem(prev.getPublicId(), prev.getEpisodeNumber());
        }

        if (next != null) {
            nextEpisode = new EpisodeDetailResponse.EpisodePrevNextItem(next.getPublicId(), next.getEpisodeNumber());
        }

        return new EpisodeDetailResponse.EpisodePrevNext(prevEpisode, nextEpisode);
    }
}
