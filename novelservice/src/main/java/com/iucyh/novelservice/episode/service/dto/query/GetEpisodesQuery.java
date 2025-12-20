package com.iucyh.novelservice.episode.service.dto.query;

import com.iucyh.novelservice.episode.enumtype.EpisodeSortType;

public record GetEpisodesQuery(

        String novelPublicId,
        EpisodeSortType sortType,
        int page,
        int limit
) {}
