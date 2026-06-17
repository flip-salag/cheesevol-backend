package com.iucyh.flip.episode.web.dto.request;

import com.iucyh.flip.common.vo.HtmlContent;
import com.iucyh.flip.core.json.deserializer.html.SanitizedHtml;
import com.iucyh.flip.core.validator.htmlnotblank.NotBlankWithoutHtml;
import jakarta.validation.constraints.NotNull;

import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_CONTENT_SAFE_LIST_KEY;

public record UpdateEpisodeContentRequest(

        @NotNull
        @NotBlankWithoutHtml
        @SanitizedHtml(EPISODE_CONTENT_SAFE_LIST_KEY)
        HtmlContent content
) {}
