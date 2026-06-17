package com.iucyh.novelservice.episode.service.dto.command;

import com.iucyh.novelservice.common.vo.HtmlContent;
import com.iucyh.novelservice.episode.enumtype.EpisodeType;

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
