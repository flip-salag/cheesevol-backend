package com.iucyh.novelservice.episode.service.dto.command;

public record CreateEpisodeCommand(

        long userId,
        String novelPublicId,
        String title,
        String description,
        String content
) {
    public CreateEpisodeCommand {
        title = title.strip();
    }
}
