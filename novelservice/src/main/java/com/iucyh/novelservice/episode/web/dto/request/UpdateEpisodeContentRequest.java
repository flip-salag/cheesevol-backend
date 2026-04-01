package com.iucyh.novelservice.episode.web.dto.request;

import com.iucyh.novelservice.common.vo.HtmlContent;
import com.iucyh.novelservice.core.json.deserializer.html.SanitizedHtml;
import com.iucyh.novelservice.core.validator.htmlnotblank.NotBlankWithoutHtml;
import jakarta.validation.constraints.NotNull;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.EPISODE_CONTENT_SAFE_LIST_KEY;

public record UpdateEpisodeContentRequest(

        @NotNull
        @NotBlankWithoutHtml
        @SanitizedHtml(EPISODE_CONTENT_SAFE_LIST_KEY)
        HtmlContent content
) {}
