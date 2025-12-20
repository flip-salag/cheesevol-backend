package com.iucyh.novelservice.episode.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.episode.exception.errorcode.EpisodeErrorCode;

import java.util.Map;

public class EpisodeNotFound extends ServiceException {

    public EpisodeNotFound(String episodeId) {
        super(
                EpisodeErrorCode.EPISODE_NOT_FOUND,
                Map.of("episodeId", episodeId)
        );
    }
}
