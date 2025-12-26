package com.iucyh.novelservice.episode.service.dto.command;

import com.iucyh.novelservice.common.vo.HtmlContent;

public record UpdateEpisodeContentCommand(

        long userId,
        String episodePublicId,
        HtmlContent content
) {}
