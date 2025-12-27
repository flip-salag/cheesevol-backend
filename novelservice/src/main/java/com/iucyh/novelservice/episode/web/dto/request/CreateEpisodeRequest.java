package com.iucyh.novelservice.episode.web.dto.request;

import com.iucyh.novelservice.common.deserializer.html.SanitizedHtml;
import com.iucyh.novelservice.common.validator.htmlnotblank.NotBlankWithoutHtml;
import com.iucyh.novelservice.common.validator.htmlsize.SizeWithoutHtml;
import com.iucyh.novelservice.common.vo.HtmlContent;
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

        @NotNull
        @NotBlankWithoutHtml
        @SizeWithoutHtml(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        @SanitizedHtml(EPISODE_CONTENT_SAFE_LIST_KEY)
        HtmlContent content
) {}
