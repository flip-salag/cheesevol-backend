package com.iucyh.flip.episode.repository.custom.condition;

import com.iucyh.flip.episode.enumtype.EpisodeSortType;
import org.springframework.data.domain.Pageable;

public record EpisodePagingCondition(

        Pageable pageable,
        EpisodeSortType sortType
) {}
