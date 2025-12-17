package com.iucyh.novelservice.episode.web.dto.mapper;

import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.episode.web.dto.request.CreateEpisodeRequest;

public class EpisodeRequestMapper {

    private EpisodeRequestMapper() {}

    public static CreateEpisodeCommand toCreateEpisodeCommand(CreateEpisodeRequest request, long userId, String novelPublicId) {
        return new CreateEpisodeCommand(
                userId,
                novelPublicId,
                request.title(),
                request.description(),
                request.content()
        );
    }
}
