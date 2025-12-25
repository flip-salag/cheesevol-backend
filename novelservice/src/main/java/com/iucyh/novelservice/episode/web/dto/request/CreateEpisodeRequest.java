package com.iucyh.novelservice.episode.web.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iucyh.novelservice.common.deserializer.html.HtmlDeserializer;
import com.iucyh.novelservice.common.deserializer.html.HtmlSanitized;
import com.iucyh.novelservice.common.util.html.HtmlContentType;
import com.iucyh.novelservice.common.validator.htmlsize.SizeWithoutHtml;
import com.iucyh.novelservice.episode.constant.EpisodeConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.*;

public record CreateEpisodeRequest(

        @NotBlank
        @Size(min = EPISODE_TITLE_LENGTH_MIN, max = EpisodeConstants.EPISODE_TITLE_LENGTH_MAX)
        String title,

        @NotNull
        @Size(max = EPISODE_DESC_LENGTH_MAX)
        String description,

        @NotBlank
        @SizeWithoutHtml(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        @JsonDeserialize(using = HtmlDeserializer.class)
        @HtmlSanitized(HtmlContentType.EPISODE_CONTENT)
        String content
) {}
