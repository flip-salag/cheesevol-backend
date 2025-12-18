package com.iucyh.novelservice.episode.web.controller;

import com.iucyh.novelservice.common.response.api.ApiResponseMapper;
import com.iucyh.novelservice.common.response.api.SuccessResponse;
import com.iucyh.novelservice.episode.service.EpisodeQueryService;
import com.iucyh.novelservice.episode.service.EpisodeService;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.DeleteEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeContentCommand;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeRequestMapper;
import com.iucyh.novelservice.episode.web.dto.request.CreateEpisodeRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeContentRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeRequest;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
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

    @GetMapping("/episodes/{episodePublicId}")
    public SuccessResponse<EpisodeDetailResponse> getEpisodeDetail(
            @PathVariable String episodePublicId
    ) {
        EpisodeDetailResponse result = episodeQueryService.getEpisodeDetail(episodePublicId);
        return ApiResponseMapper.success(result);
    }

    @PostMapping("/novels/{novelPublicId}/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EpisodeSummaryResponse> createEpisode(
            @PathVariable String novelPublicId,
            @Valid @RequestBody CreateEpisodeRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        CreateEpisodeCommand command = EpisodeRequestMapper.toCreateEpisodeCommand(request, userId, novelPublicId);
        EpisodeSummaryResponse createdEpisode = episodeService.createEpisode(command);
        return ApiResponseMapper.success(createdEpisode);
    }

    @PatchMapping("/episodes/{episodePublicId}")
    public SuccessResponse<EpisodeSummaryResponse> updateEpisode(
            @PathVariable String episodePublicId,
            @Valid @RequestBody UpdateEpisodeRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        UpdateEpisodeCommand command = EpisodeRequestMapper.toUpdateEpisodeCommand(request, userId, episodePublicId);
        EpisodeSummaryResponse updatedEpisode = episodeService.updateEpisode(command);
        return ApiResponseMapper.success(updatedEpisode);
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
