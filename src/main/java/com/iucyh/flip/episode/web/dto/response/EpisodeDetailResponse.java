package com.iucyh.flip.episode.web.dto.response;

import com.iucyh.flip.episode.enumtype.EpisodeType;
import com.iucyh.flip.user.web.dto.response.info.UserBasicInfo;

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
