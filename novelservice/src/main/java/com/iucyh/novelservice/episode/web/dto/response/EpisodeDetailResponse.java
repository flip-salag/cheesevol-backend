package com.iucyh.novelservice.episode.web.dto.response;

import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;

import java.time.LocalDateTime;

public record EpisodeDetailResponse(

        String episodeId,
        int episodeNumber,
        EpisodeType episodeType,
        String title,
        String description,
        LocalDateTime publishedAt,
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
