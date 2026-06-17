package com.iucyh.flip.episode.web.dto.request;

import com.iucyh.flip.episode.enumtype.EpisodeSortType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Range;

public record EpisodePageRequest(

        @NotNull(message = "Sort is required")
        EpisodeSortType sort,

        @NotNull(message = "Page is required")
        @PositiveOrZero
        Integer page,

        @Range(min = 5, max = 20)
        Integer size
) {
    public EpisodePageRequest {
        if (size == null) {
            size = 20;
        }
    }
}
