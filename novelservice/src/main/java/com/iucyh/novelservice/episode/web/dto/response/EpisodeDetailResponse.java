package com.iucyh.novelservice.episode.web.dto.response;

import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;

import java.time.LocalDateTime;

public record EpisodeDetailResponse(

        String episodeId,
        String title,
        String description,
        int episodeNumber,
        LocalDateTime createdAt,
        NovelInfo novel,
        EpisodeNavigation navigation
) {
    public record NovelInfo(

            String novelId,
            UserBasicInfo author,
            String title,
            int likeCount,
            int totalEpisodeCount
    ) {}

    public record EpisodeNavigation(

            EpisodeNavigationItem prevEpisode,
            EpisodeNavigationItem nextEpisode
    ) {}

    public record EpisodeNavigationItem(

            String episodeId,
            int episodeNumber
    ) {}
}
