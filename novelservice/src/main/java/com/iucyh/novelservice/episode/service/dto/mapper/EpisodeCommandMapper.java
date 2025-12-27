package com.iucyh.novelservice.episode.service.dto.mapper;

import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.novel.domain.Novel;

public class EpisodeCommandMapper {

    private EpisodeCommandMapper() {}

    public static Episode toEpisode(CreateEpisodeCommand command, Novel novel, int episodeNumber) {
        return Episode.of(
                command.episodeType(),
                command.title(),
                command.description(),
                command.content().getSanitizedValue(),
                episodeNumber,
                novel
        );
    }
}
