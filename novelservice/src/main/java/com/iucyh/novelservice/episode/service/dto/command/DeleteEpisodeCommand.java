package com.iucyh.novelservice.episode.service.dto.command;

public record DeleteEpisodeCommand(

        long userId,
        String episodePublicId
) {}
