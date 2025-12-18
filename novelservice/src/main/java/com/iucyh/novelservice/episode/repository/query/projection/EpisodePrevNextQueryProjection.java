package com.iucyh.novelservice.episode.repository.query.projection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EpisodePrevNextQueryProjection {

    private final String episodePublicId;
    private final Integer episodeNumber;
}
