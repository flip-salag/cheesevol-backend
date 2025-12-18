package com.iucyh.novelservice.episode.service.dto.query;

import com.iucyh.novelservice.episode.enumtype.EpisodeSortType;

public record GetEpisodesQuery(

        EpisodeSortType sortType,
        int page,
        int limit
) {}
