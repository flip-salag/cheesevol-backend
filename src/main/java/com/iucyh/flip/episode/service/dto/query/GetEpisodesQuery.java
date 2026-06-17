package com.iucyh.flip.episode.service.dto.query;

import com.iucyh.flip.episode.enumtype.EpisodeSortType;

public record GetEpisodesQuery(

        String novelPublicId,
        EpisodeSortType sortType,
        int page,
        int limit
) {}
