package com.iucyh.flip.episode.web.dto.request;

import com.iucyh.flip.common.vo.HtmlContent;
import com.iucyh.flip.core.json.deserializer.html.SanitizedHtml;
import com.iucyh.flip.core.validator.enumfield.EnumField;
import com.iucyh.flip.core.validator.htmlnotblank.NotBlankWithoutHtml;
import com.iucyh.flip.episode.constant.EpisodeConstants;
import com.iucyh.flip.episode.enumtype.EpisodeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_CONTENT_SAFE_LIST_KEY;
import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_DESC_LENGTH_MAX;
import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_TITLE_LENGTH_MIN;

public record CreateEpisodeRequest(

        @NotNull
        @EnumField(enumClass = EpisodeType.class)
        String episodeType,

        @NotBlank
        @Size(min = EPISODE_TITLE_LENGTH_MIN, max = EpisodeConstants.EPISODE_TITLE_LENGTH_MAX)
        String title,

        @NotNull
        @Size(max = EPISODE_DESC_LENGTH_MAX)
        String description,

        @NotNull
        @NotBlankWithoutHtml
        @SanitizedHtml(EPISODE_CONTENT_SAFE_LIST_KEY)
        HtmlContent content
) {}
