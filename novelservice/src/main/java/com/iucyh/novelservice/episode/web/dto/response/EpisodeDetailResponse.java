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
        EpisodePrevNext prevNext
) {
    public record NovelInfo(

            String novelId,
            UserBasicInfo author,
            String title,
            int likeCount
    ) {}

    public record EpisodePrevNext(

            EpisodePrevNextItem prevEpisode,
            EpisodePrevNextItem nextEpisode
    ) {}

    public record EpisodePrevNextItem(

            String episodeId,
            int episodeNumber
    ) {}
}
