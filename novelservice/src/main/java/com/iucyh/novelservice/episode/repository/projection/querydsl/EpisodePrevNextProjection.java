package com.iucyh.novelservice.episode.repository.projection.querydsl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EpisodePrevNextProjection {

    private final String publicId;
    private final Integer episodeNumber;
}
