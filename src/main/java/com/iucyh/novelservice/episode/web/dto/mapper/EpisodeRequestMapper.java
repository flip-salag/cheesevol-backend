package com.iucyh.novelservice.episode.web.dto.mapper;

import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.DeleteEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeContentCommand;
import com.iucyh.novelservice.episode.service.dto.query.GetEpisodesQuery;
import com.iucyh.novelservice.episode.web.dto.request.CreateEpisodeRequest;
import com.iucyh.novelservice.episode.web.dto.request.EpisodePageRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeContentRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeRequest;

public class EpisodeRequestMapper {

    private EpisodeRequestMapper() {}

    public static CreateEpisodeCommand toCreateEpisodeCommand(CreateEpisodeRequest request, long userId, String novelPublicId) {
        return new CreateEpisodeCommand(
                userId,
                EpisodeType.of(request.episodeType()),
                novelPublicId,
                request.title(),
                request.description(),
                request.content()
        );
    }

    public static UpdateEpisodeCommand toUpdateEpisodeCommand(UpdateEpisodeRequest request, long userId, String episodePublicId) {
        return new UpdateEpisodeCommand(
                userId,
                episodePublicId,
                request.title(),
                request.description()
        );
    }

    public static UpdateEpisodeContentCommand toUpdateEpisodeContentCommand(UpdateEpisodeContentRequest request, long userId, String episodePublicId) {
        return new UpdateEpisodeContentCommand(
                userId,
                episodePublicId,
                request.content()
        );
    }

    public static DeleteEpisodeCommand toDeleteEpisodeCommand(long userId, String episodePublicId) {
        return new DeleteEpisodeCommand(userId, episodePublicId);
    }

    public static GetEpisodesQuery toGetEpisodesQuery(EpisodePageRequest request, String novelPublicId) {
        return new GetEpisodesQuery(
                novelPublicId,
                request.sort(),
                request.page(),
                request.size()
        );
    }
}
