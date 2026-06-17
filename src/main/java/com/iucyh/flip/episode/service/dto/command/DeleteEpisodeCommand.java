package com.iucyh.flip.episode.service.dto.command;

public record DeleteEpisodeCommand(

        long userId,
        String episodePublicId
) {}
