package com.iucyh.novelservice.episode.service.policy;

import com.iucyh.novelservice.common.vo.HtmlContent;
import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.exception.EpisodeContentLengthNotValid;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.*;
import static com.iucyh.novelservice.episode.constant.EpisodeConstants.PROLOGUE_EPISODE_CONTENT_LENGTH_MAX;

@Component
public class EpisodePolicyValidator {

    /**
     * <p>회차의 본문 길이가 유효한지 {@code EpisodeType}에 맞는 기준으로 검증</p>
     * @param episodeType 검사할 본문을 가지고 있는 회차의 {@code EpisodeType}
     * @param content 검사할 본문
     * @throws EpisodeContentLengthNotValid 회차의 길이가 유효하지 않을때(너무 짧거나, 너무 길때)
     */
    public void validateContentLength(EpisodeType episodeType, HtmlContent content) throws EpisodeContentLengthNotValid {
        int min = 0;
        int max = 0;

        switch (episodeType) {
            case COMMON -> {
                min = COMMON_EPISODE_CONTENT_LENGTH_MIN;
                max = COMMON_EPISODE_CONTENT_LENGTH_MAX;
            }

            case PROLOGUE -> {
                min = PROLOGUE_EPISODE_CONTENT_LENGTH_MIN;
                max = PROLOGUE_EPISODE_CONTENT_LENGTH_MAX;
            }
        }

        String textValue = content.getTextValue();
        boolean isValid = textValue.length() >= min && textValue.length() <= max;
        if (!isValid) {
            throw new EpisodeContentLengthNotValid(episodeType, min, max);
        }
    }
}
