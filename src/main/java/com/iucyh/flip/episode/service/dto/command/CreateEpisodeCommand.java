package com.iucyh.flip.episode.service.dto.command;

import com.iucyh.flip.common.vo.HtmlContent;
import com.iucyh.flip.episode.enumtype.EpisodeType;

public record CreateEpisodeCommand(

        long userId,
        EpisodeType episodeType,
        String novelPublicId,
        String title,
        String description,
        HtmlContent content
) {
    public CreateEpisodeCommand {
        title = title.strip();
    }
}
