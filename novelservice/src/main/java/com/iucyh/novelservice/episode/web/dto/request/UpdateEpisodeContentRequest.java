package com.iucyh.novelservice.episode.web.dto.request;

import com.iucyh.novelservice.common.deserializer.html.SanitizedHtml;
import com.iucyh.novelservice.common.validator.htmlnotblank.NotBlankWithoutHtml;
import com.iucyh.novelservice.common.validator.htmlsize.SizeWithoutHtml;
import com.iucyh.novelservice.common.vo.HtmlContent;
import jakarta.validation.constraints.NotNull;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.*;

public record UpdateEpisodeContentRequest(

        @NotNull
        @NotBlankWithoutHtml
        @SizeWithoutHtml(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        @SanitizedHtml(EPISODE_CONTENT_SAFE_LIST_KEY)
        HtmlContent content
) {}
