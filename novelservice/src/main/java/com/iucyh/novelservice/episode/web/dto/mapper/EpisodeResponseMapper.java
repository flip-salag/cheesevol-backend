package com.iucyh.novelservice.episode.web.dto.mapper;

import com.iucyh.novelservice.common.response.PageWithOffsetResponse;
import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodeDetailQueryProjection;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodePrevNextQueryProjection;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodeSummaryQueryProjection;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public class EpisodeResponseMapper {

    private EpisodeResponseMapper() {}

    public static EpisodeSummaryResponse toEpisodeSummaryResponse(Episode episode) {
        return new EpisodeSummaryResponse(
                episode.getPublicId(),
                episode.getEpisodeNumber(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getViewCount(),
                episode.getCreatedAt()
        );
    }

    public static EpisodeSummaryResponse toEpisodeSummaryResponse(EpisodeSummaryQueryProjection episode) {
        return new EpisodeSummaryResponse(
                episode.getPublicId(),
                episode.getEpisodeNumber(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getViewCount(),
                episode.getCreatedAt()
        );
    }

    public static PageWithOffsetResponse<EpisodeSummaryResponse> toEpisodeSummaryResponse(Page<EpisodeSummaryQueryProjection> page) {
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

    public static EpisodeDetailResponse toEpisodeDetailResponse(EpisodeDetailQueryProjection episodeDetail, EpisodePrevNextQueryProjection prev, EpisodePrevNextQueryProjection next) {
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
                episodeDetail.getEpisodeTitle(),
                episodeDetail.getEpisodeDescription(),
                episodeDetail.getEpisodeNumber(),
                episodeDetail.getEpisodeCreatedAt(),
                novelInfo,
                episodePrevNext
        );
    }

    private static EpisodeDetailResponse.EpisodePrevNext getEpisodePrevNext(EpisodePrevNextQueryProjection prev, EpisodePrevNextQueryProjection next) {
        EpisodeDetailResponse.EpisodePrevNextItem prevEpisode = null;
        EpisodeDetailResponse.EpisodePrevNextItem nextEpisode = null;

        if (prev != null) {
            prevEpisode = new EpisodeDetailResponse.EpisodePrevNextItem(prev.getEpisodePublicId(), prev.getEpisodeNumber());
        }

        if (next != null) {
            nextEpisode = new EpisodeDetailResponse.EpisodePrevNextItem(next.getEpisodePublicId(), next.getEpisodeNumber());
        }

        return new EpisodeDetailResponse.EpisodePrevNext(prevEpisode, nextEpisode);
    }
}
