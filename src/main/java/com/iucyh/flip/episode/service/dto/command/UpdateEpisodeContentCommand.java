package com.iucyh.flip.episode.service.dto.command;

import com.iucyh.flip.common.vo.HtmlContent;

public record UpdateEpisodeContentCommand(

        long userId,
        String episodePublicId,
        HtmlContent content
) {}
