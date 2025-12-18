package com.iucyh.novelservice.episode.service;

import com.iucyh.novelservice.episode.exception.EpisodeNotFound;
import com.iucyh.novelservice.episode.repository.query.condition.EpisodeSearchCondition;
import com.iucyh.novelservice.common.response.PageResponse;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodeDetailQueryProjection;
import com.iucyh.novelservice.episode.repository.query.projection.EpisodePrevNextQueryProjection;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.episode.web.dto.request.EpisodePagingRequest;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.episode.repository.projection.EpisodeDetail;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.service.NovelViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EpisodeQueryService {

    private final NovelViewCountService novelViewCountService;
    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;
    private final EpisodeQueryRepository episodeQueryRepository;

    public PageResponse<EpisodeSummaryResponse> findEpisodesByNovel(long novelId, EpisodePagingRequest request) {
        boolean novelExists = novelRepository.existsById(novelId);
        if (!novelExists) {
            //throw new NovelNotFound(novelId);
        }

        EpisodeSearchCondition searchCondition = new EpisodeSearchCondition(request.lastEpisode(), request.limit());
        List<EpisodeSimpleQueryDto> result = episodeQueryRepository.findEpisodesByNovelId(novelId, searchCondition);
        int episodeCount = episodeRepository.countByNovelId(novelId);

        if (result.isEmpty()) {
            return EpisodeResponseMapper.toPagingResponse(List.of(), episodeCount, null);
        }

        List<EpisodeSummaryResponse> episodeResponses = mapToEpisodeResponseList(result);
        int lastEpisodeNumber = result.get(result.size() - 1).getEpisodeNumber();
        return EpisodeResponseMapper.toPagingResponse(episodeResponses, episodeCount, lastEpisodeNumber);
    }

    public EpisodeDetailResponse findEpisodeDetail(String episodePublicId) {
        EpisodeDetailQueryProjection detailResult = episodeQueryRepository.findEpisodeDetailByPublicId(episodePublicId)
                .orElseThrow(() -> new EpisodeNotFound(episodePublicId));

        EpisodePrevNextQueryProjection prevEpisode = episodeQueryRepository.findPrevEpisode(detailResult.getNovelId(), detailResult.getEpisodeNumber())
                .orElse(null);
        EpisodePrevNextQueryProjection nextEpisode = episodeQueryRepository.findNextEpisode(detailResult.getNovelId(), detailResult.getEpisodeNumber())
                .orElse(null);

        return EpisodeResponseMapper.toEpisodeDetailResponse(detailResult, prevEpisode, nextEpisode);
    }

    private List<EpisodeSummaryResponse> mapToEpisodeResponseList(List<EpisodeSimpleQueryDto> episodes) {
        return episodes.stream()
                .map(EpisodeResponseMapper::toEpisodeSummaryResponse)
                .toList();
    }
}
