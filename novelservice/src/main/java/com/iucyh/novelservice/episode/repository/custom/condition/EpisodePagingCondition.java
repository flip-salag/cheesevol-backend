package com.iucyh.novelservice.episode.repository.custom.condition;

import com.iucyh.novelservice.episode.enumtype.EpisodeSortType;
import org.springframework.data.domain.Pageable;

public record EpisodePagingCondition(

        Pageable pageable,
        EpisodeSortType sortType
) {}
