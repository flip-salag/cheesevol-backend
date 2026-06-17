package com.iucyh.novelservice.episode.exception;

import com.iucyh.novelservice.base.exception.BusinessException;
import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.exception.errorcode.EpisodeErrorCode;

public class EpisodeContentLengthNotValid extends BusinessException {

    /**
     * @param episodeType 검증 당시 기준이 되었던 {@code EpisodeType}
     * @param min 해당 {@code EpisodeType}에서 허용되는 최소 본문 길이
     * @param max 해당 {@code EpisodeType}에서 허용되는 최대 본문 길이
     */
    public EpisodeContentLengthNotValid(EpisodeType episodeType, int min, int max) {
        super(
                EpisodeErrorCode.EPISODE_CONTENT_LENGTH_NOT_VALID,
                "Episode content length must be between %d and %d in type: %s".formatted(min, max, episodeType.getValue())
        );
    }
}
