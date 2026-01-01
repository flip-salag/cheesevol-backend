package com.iucyh.novelservice.episode.web.controller;

import com.iucyh.novelservice.common.response.PageWithOffsetResponse;
import com.iucyh.novelservice.base.response.api.ApiResponseMapper;
import com.iucyh.novelservice.base.response.api.SuccessResponse;
import com.iucyh.novelservice.episode.service.EpisodeQueryService;
import com.iucyh.novelservice.episode.service.EpisodeService;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.DeleteEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeContentCommand;
import com.iucyh.novelservice.episode.service.dto.query.GetEpisodesQuery;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeRequestMapper;
import com.iucyh.novelservice.episode.web.dto.request.CreateEpisodeRequest;
import com.iucyh.novelservice.episode.web.dto.request.EpisodePageRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeContentRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeRequest;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeContentResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSaveResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EpisodeController {

    private static final String TEMP_USER_ID_HEADER = "TEMP-USER-ID";

    private final EpisodeService episodeService;
    private final EpisodeQueryService episodeQueryService;

    @GetMapping("/novels/{novelPublicId}/episodes")
    public SuccessResponse<PageWithOffsetResponse<EpisodeSummaryResponse>> getEpisodesByNovel(
            @PathVariable String novelPublicId,
            @Valid @ModelAttribute EpisodePageRequest request
    ) {
        GetEpisodesQuery query = EpisodeRequestMapper.toGetEpisodesQuery(request, novelPublicId);
        PageWithOffsetResponse<EpisodeSummaryResponse> result = episodeQueryService.getEpisodesByNovel(query);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/episodes/{episodePublicId}")
    public SuccessResponse<EpisodeDetailResponse> getEpisodeDetail(
            @PathVariable String episodePublicId
    ) {
        EpisodeDetailResponse result = episodeQueryService.getEpisodeDetail(episodePublicId);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/episodes/{episodePublicId}/content")
    public SuccessResponse<EpisodeContentResponse> getEpisodeContent(
            @PathVariable String episodePublicId
    ) {
        EpisodeContentResponse result = episodeQueryService.getEpisodeContent(episodePublicId);
        return ApiResponseMapper.success(result);
    }

    @PostMapping("/novels/{novelPublicId}/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EpisodeSaveResponse> createEpisode(
            @PathVariable String novelPublicId,
            @Valid @RequestBody CreateEpisodeRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        CreateEpisodeCommand command = EpisodeRequestMapper.toCreateEpisodeCommand(request, userId, novelPublicId);
        EpisodeSaveResponse result = episodeService.createEpisode(command);
        return ApiResponseMapper.success(result);
    }

    @PatchMapping("/episodes/{episodePublicId}")
    public SuccessResponse<EpisodeSaveResponse> updateEpisode(
            @PathVariable String episodePublicId,
            @Valid @RequestBody UpdateEpisodeRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        UpdateEpisodeCommand command = EpisodeRequestMapper.toUpdateEpisodeCommand(request, userId, episodePublicId);
        EpisodeSaveResponse result = episodeService.updateEpisode(command);
        return ApiResponseMapper.success(result);
    }

    @PutMapping("/episodes/{episodePublicId}/content")
    public SuccessResponse<Void> updateEpisodeContent(
            @PathVariable String episodePublicId,
            @Valid @RequestBody UpdateEpisodeContentRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        UpdateEpisodeContentCommand command = EpisodeRequestMapper.toUpdateEpisodeContentCommand(request, userId, episodePublicId);
        episodeService.updateEpisodeContent(command);
        return ApiResponseMapper.success();
    }

    @DeleteMapping("/episodes/{episodePublicId}")
    public SuccessResponse<Void> deleteEpisode(
            @PathVariable String episodePublicId,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        DeleteEpisodeCommand command = EpisodeRequestMapper.toDeleteEpisodeCommand(userId, episodePublicId);
        episodeService.deleteEpisode(command);
        return ApiResponseMapper.success();
    }
}
