package com.iucyh.novelservice.novel.web.controller;

import com.iucyh.novelservice.common.dto.apiresponse.ApiResponseMapper;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.common.dto.response.PagingResponse;
import com.iucyh.novelservice.common.dto.apiresponse.SuccessResponse;
import com.iucyh.novelservice.episode.web.dto.request.CreateEpisodeRequest;
import com.iucyh.novelservice.episode.web.dto.request.EpisodePagingRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeDetailRequest;
import com.iucyh.novelservice.episode.web.dto.request.UpdateEpisodeRequest;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeResponse;
import com.iucyh.novelservice.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.UpdateNovelCommand;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelRequestMapper;
import com.iucyh.novelservice.novel.web.dto.request.CreateNovelRequest;
import com.iucyh.novelservice.novel.web.dto.request.NovelPagingRequest;
import com.iucyh.novelservice.novel.web.dto.request.UpdateNovelCompletionRequest;
import com.iucyh.novelservice.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.request.UpdateNovelRequest;
import com.iucyh.novelservice.novel.web.dto.response.NovelResponse;
import com.iucyh.novelservice.episode.service.EpisodeQueryService;
import com.iucyh.novelservice.episode.service.EpisodeService;
import com.iucyh.novelservice.novel.service.NovelQueryService;
import com.iucyh.novelservice.novel.service.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/novels")
@RequiredArgsConstructor
public class NovelController {

    private static final String TEMP_USER_ID_HEADER = "TEMP-USER-ID";

    private final NovelService novelService;
    private final EpisodeService episodeService;
    private final NovelQueryService novelQueryService;
    private final EpisodeQueryService episodeQueryService;

    @GetMapping
    public SuccessResponse<PagingResponse<NovelResponse>> getNovels(
            @Valid @ModelAttribute NovelPagingRequest request
    ) {
        //PagingResponse<NovelResponse> results = novelQueryService.findNovels(request);
        return ApiResponseMapper.success(null);
    }

    @GetMapping("/new")
    public SuccessResponse<PagingResponse<NovelResponse>> getNewNovels(
            @Valid @ModelAttribute NovelPagingRequest request
    ) {
        PagingResponse<NovelResponse> results = novelQueryService.findNewNovels(request);
        return ApiResponseMapper.success(results);
    }

    @GetMapping("/category/{category}")
    public SuccessResponse<PagingResponse<NovelResponse>> getNovelsByCategory(
            @PathVariable NovelCategory category,
            @Valid @ModelAttribute NovelPagingRequest request
    ) {
        PagingResponse<NovelResponse> results = novelQueryService.findNovelsByCategory(request, category);
        return ApiResponseMapper.success(results);
    }

    @GetMapping("/{novelId}/episodes")
    public SuccessResponse<PagingResponse<EpisodeResponse>> getEpisodes(
            @PathVariable long novelId,
            @Valid @ModelAttribute EpisodePagingRequest request
    ) {
        PagingResponse<EpisodeResponse> result = episodeQueryService.findEpisodesByNovel(novelId, request);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/{novelId}/episodes/{episodeNumber}")
    public SuccessResponse<EpisodeDetailResponse> getEpisodeDetail(
            @PathVariable long novelId,
            @PathVariable int episodeNumber
    ) {
        EpisodeDetailResponse result = episodeQueryService.findEpisodeDetail(novelId, episodeNumber);
        return ApiResponseMapper.success(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<NovelResponse> createNovel(
            @Valid @RequestBody CreateNovelRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        CreateNovelCommand command = NovelRequestMapper.toCreateNovelCommand(request);
        NovelResponse createdNovel = novelService.createNovel(command, userId);
        return ApiResponseMapper.success(createdNovel);
    }

    @PostMapping("/{novelId}/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EpisodeResponse> createEpisode(
            @PathVariable long novelId,
            @Valid @RequestBody CreateEpisodeRequest request
    ) {
        EpisodeResponse result = episodeService.createEpisode(novelId, request);
        return ApiResponseMapper.success(result);
    }

    @PatchMapping("/{novelPublicId}")
    public SuccessResponse<NovelResponse> updateNovel(
            @PathVariable String novelPublicId,
            @Valid @RequestBody UpdateNovelRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        UpdateNovelCommand command = NovelRequestMapper.toUpdateNovelCommand(request);
        NovelResponse updatedNovel = novelService.updateNovel(command, userId, novelPublicId);
        return ApiResponseMapper.success(updatedNovel);
    }

    @PutMapping("/{novelPublicId}/completion")
    public SuccessResponse<NovelCompletionResponse> updateNovelCompletion(
            @PathVariable String novelPublicId,
            @Valid @RequestBody UpdateNovelCompletionRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        NovelCompletionResponse completionResponse = novelService.updateNovelCompletion(request.isCompleted(), userId, novelPublicId);
        return ApiResponseMapper.success(completionResponse);
    }

    @PatchMapping("/{novelId}/episodes/{episodeId}")
    public SuccessResponse<EpisodeResponse> updateEpisode(
            @PathVariable long novelId,
            @PathVariable long episodeId,
            @Valid @RequestBody UpdateEpisodeRequest request
    ) {
        EpisodeResponse result = episodeService.updateEpisode(novelId, episodeId, request);
        return ApiResponseMapper.success(result);
    }

    @PatchMapping("/{novelId}/episodes/{episodeId}/detail")
    public SuccessResponse<EpisodeDetailResponse> updateEpisodeDetail(
            @PathVariable long novelId,
            @PathVariable long episodeId,
            @Valid @RequestBody UpdateEpisodeDetailRequest request
    ) {
        EpisodeDetailResponse result = episodeService.updateEpisodeDetail(novelId, episodeId, request);
        return ApiResponseMapper.success(result);
    }

    @PostMapping("/{novelId}/likes")
    public SuccessResponse<NovelLikeCountResponse> addLikeCount(
            @PathVariable long novelId
    ) {
        NovelLikeCountResponse result = novelService.addLikeCount(1, novelId);
        return ApiResponseMapper.success(result);
    }

    @DeleteMapping("/{novelId}/likes")
    public SuccessResponse<NovelLikeCountResponse> removeLikeCount(
            @PathVariable long novelId
    ) {
        NovelLikeCountResponse result = novelService.removeLikeCount(1, novelId);
        return ApiResponseMapper.success(result);
    }

    @DeleteMapping("/{novelPublicId}")
    public SuccessResponse<Void> deleteNovel(
            @PathVariable String novelPublicId,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        novelService.deleteNovel(userId, novelPublicId);
        return ApiResponseMapper.success();
    }

    @DeleteMapping("/{novelId}/episodes/{episodeId}")
    public SuccessResponse<Void> deleteEpisode(
            @PathVariable long novelId,
            @PathVariable long episodeId
    ) {
        episodeService.deleteEpisode(novelId, episodeId);
        return ApiResponseMapper.success();
    }
}
