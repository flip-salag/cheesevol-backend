package com.iucyh.novelservice.episode.service.dto.command;

import com.iucyh.novelservice.common.vo.HtmlContent;

public record CreateEpisodeCommand(

        long userId,
        String novelPublicId,
        String title,
        String description,
        HtmlContent content
) {
    public CreateEpisodeCommand {
        title = title.strip();
    }
}
