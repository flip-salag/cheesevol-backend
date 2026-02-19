package com.iucyh.novelservice.episode.service;

import com.iucyh.novelservice.common.exception.DataNotFound;
import com.iucyh.novelservice.common.response.PageWithOffsetResponse;
import com.iucyh.novelservice.episode.repository.projection.querydsl.EpisodeDetailProjection;
import com.iucyh.novelservice.episode.repository.projection.querydsl.EpisodeSummaryProjection;
import com.iucyh.novelservice.episode.repository.query.condition.EpisodePagingCondition;
import com.iucyh.novelservice.episode.repository.projection.querydsl.EpisodePrevNextProjection;
import com.iucyh.novelservice.episode.service.dto.query.GetEpisodesQuery;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeContentResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.novel.repository.query.NovelQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // TODO: 조회수 관련 로직 구현
public class EpisodeQueryService {

    private final NovelQueryRepository novelQueryRepository;
    private final EpisodeQueryRepository episodeQueryRepository;

    public PageWithOffsetResponse<EpisodeSummaryResponse> getEpisodesByNovel(GetEpisodesQuery query) {
        boolean novelExists = novelQueryRepository.existsByPublicId(query.novelPublicId());
        if (!novelExists) {
            throw new DataNotFound(query.novelPublicId()); // 소설이 유효하지 않거나 존재하지 않으면 없는 것으로 간주 (자세한 정책은 existsByPublicId 메서드 주석 확인)
        }

        Pageable pageable = PageRequest.of(query.page(), query.limit());
        EpisodePagingCondition condition = new EpisodePagingCondition(pageable, query.sortType());
        Page<EpisodeSummaryProjection> result = episodeQueryRepository.findEpisodesByNovelPublicId(query.novelPublicId(), condition);

        return EpisodeResponseMapper.toPageResponse(result);
    }

    public EpisodeDetailResponse getEpisodeDetail(String episodePublicId) {
        EpisodeDetailProjection detailResult = episodeQueryRepository.findEpisodeDetailByPublicId(episodePublicId)
                .orElseThrow(() -> new DataNotFound(episodePublicId));

        EpisodePrevNextProjection prevEpisode = episodeQueryRepository.findPrevEpisode(detailResult.getNovelId(), detailResult.getEpisodeNumber())
                .orElse(null);
        EpisodePrevNextProjection nextEpisode = episodeQueryRepository.findNextEpisode(detailResult.getNovelId(), detailResult.getEpisodeNumber())
                .orElse(null);

        return EpisodeResponseMapper.toEpisodeDetailResponse(detailResult, prevEpisode, nextEpisode);
    }

    public EpisodeContentResponse getEpisodeContent(String episodePublicId) {
        String contentResult = episodeQueryRepository.findEpisodeContentByPublicId(episodePublicId)
                .orElseThrow(() -> new DataNotFound(episodePublicId));
        return EpisodeResponseMapper.toEpisodeContentResponse(episodePublicId, contentResult);
    }
}
