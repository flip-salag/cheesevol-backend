package com.iucyh.novelservice.episode.repository.projection.querydsl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EpisodePrevNextQueryProjection {

    private final String publicId;
    private final Integer episodeNumber;
}
