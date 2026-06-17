package com.iucyh.flip.episode.web.dto.request;

import com.iucyh.flip.core.validator.notblank.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_DESC_LENGTH_MAX;
import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_TITLE_LENGTH_MAX;
import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_TITLE_LENGTH_MIN;

public record UpdateEpisodeRequest(

        @NotBlankIfPresent
        @Size(min = EPISODE_TITLE_LENGTH_MIN, max = EPISODE_TITLE_LENGTH_MAX)
        String title,

        @Size(max = EPISODE_DESC_LENGTH_MAX)
        String description
) {}
