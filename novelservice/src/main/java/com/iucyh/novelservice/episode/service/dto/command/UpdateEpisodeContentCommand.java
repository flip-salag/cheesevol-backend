package com.iucyh.novelservice.episode.service.dto.command;

public record UpdateEpisodeContentCommand(

        long userId,
        String episodePublicId,
        String content
) {}
