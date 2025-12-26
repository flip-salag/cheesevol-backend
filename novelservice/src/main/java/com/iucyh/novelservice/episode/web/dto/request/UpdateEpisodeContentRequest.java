package com.iucyh.novelservice.episode.web.dto.request;

import com.iucyh.novelservice.common.deserializer.html.HtmlSanitized;
import com.iucyh.novelservice.common.validator.htmlnotblank.NotBlankWithoutHtml;
import com.iucyh.novelservice.common.validator.htmlsize.SizeWithoutHtml;
import com.iucyh.novelservice.common.vo.HtmlContent;
import com.iucyh.novelservice.episode.constant.EpisodeConstants;
import jakarta.validation.constraints.NotNull;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MAX;
import static com.iucyh.novelservice.episode.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MIN;

public record UpdateEpisodeContentRequest(

        @NotNull
        @NotBlankWithoutHtml
        @SizeWithoutHtml(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        @HtmlSanitized(EpisodeConstants.EPISODE_CONTENT_SAFE_LIST_KEY)
        HtmlContent content
) {}
