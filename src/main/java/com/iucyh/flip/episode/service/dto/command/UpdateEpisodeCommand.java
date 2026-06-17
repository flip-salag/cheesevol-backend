package com.iucyh.flip.episode.service.dto.command;

public record UpdateEpisodeCommand(

        long userId,
        String episodePublicId,
        String title,
        String description
) {
    public UpdateEpisodeCommand {
        if (title != null) {
            title = title.strip();
        }
    }
}
