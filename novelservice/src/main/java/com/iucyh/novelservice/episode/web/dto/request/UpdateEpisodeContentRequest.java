package com.iucyh.novelservice.episode.web.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iucyh.novelservice.common.deserializer.html.HtmlDeserializer;
import com.iucyh.novelservice.common.deserializer.html.HtmlSanitized;
import com.iucyh.novelservice.common.util.htmlsanitizer.HtmlContentType;
import com.iucyh.novelservice.common.validator.html.SizeWithoutHtml;
import jakarta.validation.constraints.NotNull;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MAX;
import static com.iucyh.novelservice.episode.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MIN;

public record UpdateEpisodeContentRequest(

        @NotNull
        @SizeWithoutHtml(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        @JsonDeserialize(using = HtmlDeserializer.class)
        @HtmlSanitized(HtmlContentType.EPISODE_CONTENT)
        String content
) {}
