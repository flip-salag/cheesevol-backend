package com.iucyh.flip.episode.service.policy;

import com.iucyh.flip.common.vo.HtmlContent;
import com.iucyh.flip.episode.enumtype.EpisodeType;
import com.iucyh.flip.episode.exception.EpisodeContentLengthNotValid;
import org.springframework.stereotype.Component;

import static com.iucyh.flip.episode.constant.EpisodeConstants.COMMON_EPISODE_CONTENT_LENGTH_MAX;
import static com.iucyh.flip.episode.constant.EpisodeConstants.COMMON_EPISODE_CONTENT_LENGTH_MIN;
import static com.iucyh.flip.episode.constant.EpisodeConstants.PROLOGUE_EPISODE_CONTENT_LENGTH_MAX;
import static com.iucyh.flip.episode.constant.EpisodeConstants.PROLOGUE_EPISODE_CONTENT_LENGTH_MIN;

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
        boolean isValid = min <= textValue.length() && textValue.length() <= max;
        if (!isValid) {
            throw new EpisodeContentLengthNotValid(episodeType, min, max);
        }
    }
}
