package com.iucyh.novelservice.novel.web.controller;

import com.iucyh.novelservice.common.response.api.ApiResponseMapper;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.common.response.PageResponse;
import com.iucyh.novelservice.common.response.api.SuccessResponse;
import com.iucyh.novelservice.episode.web.dto.request.EpisodePagingRequest;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import com.iucyh.novelservice.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.DeleteNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.UpdateNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.UpdateNovelCompletionCommand;
import com.iucyh.novelservice.novel.service.dto.query.GetNewNovelsQuery;
import com.iucyh.novelservice.novel.service.dto.query.GetNovelsQuery;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelRequestMapper;
import com.iucyh.novelservice.novel.web.dto.request.CreateNovelRequest;
import com.iucyh.novelservice.novel.web.dto.request.NovelPageRequest;
import com.iucyh.novelservice.novel.web.dto.request.UpdateNovelCompletionRequest;
import com.iucyh.novelservice.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.request.UpdateNovelRequest;
import com.iucyh.novelservice.novel.web.dto.response.NovelSummaryResponse;
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
    public SuccessResponse<PageResponse<NovelSummaryResponse>> getNovels(
            @Valid @ModelAttribute NovelPageRequest request,
            @RequestParam(required = false) NovelCategory category
    ) {
        GetNovelsQuery query = NovelRequestMapper.toGetNovelsQuery(request, category);
        PageResponse<NovelSummaryResponse> result = novelQueryService.getNovels(query);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/new")
    public SuccessResponse<PageResponse<NovelSummaryResponse>> getNewNovels(
            @Valid @ModelAttribute NovelPageRequest request,
            @RequestParam(required = false) NovelCategory category
    ) {
        GetNewNovelsQuery query = NovelRequestMapper.toGetNewNovelsQuery(request, category);
        PageResponse<NovelSummaryResponse> result = novelQueryService.getNewNovels(query);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/{novelId}/episodes")
    public SuccessResponse<PageResponse<EpisodeSummaryResponse>> getEpisodes(
            @PathVariable long novelId,
            @Valid @ModelAttribute EpisodePagingRequest request
    ) {
        PageResponse<EpisodeSummaryResponse> result = episodeQueryService.findEpisodesByNovel(novelId, request);
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
    public SuccessResponse<NovelSummaryResponse> createNovel(
            @Valid @RequestBody CreateNovelRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        CreateNovelCommand command = NovelRequestMapper.toCreateNovelCommand(request, userId);
        NovelSummaryResponse createdNovel = novelService.createNovel(command);
        return ApiResponseMapper.success(createdNovel);
    }

    @PatchMapping("/{novelPublicId}")
    public SuccessResponse<NovelSummaryResponse> updateNovel(
            @PathVariable String novelPublicId,
            @Valid @RequestBody UpdateNovelRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        UpdateNovelCommand command = NovelRequestMapper.toUpdateNovelCommand(request, userId, novelPublicId);
        NovelSummaryResponse updatedNovel = novelService.updateNovel(command);
        return ApiResponseMapper.success(updatedNovel);
    }

    @PutMapping("/{novelPublicId}/completion")
    public SuccessResponse<NovelCompletionResponse> updateNovelCompletion(
            @PathVariable String novelPublicId,
            @Valid @RequestBody UpdateNovelCompletionRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        UpdateNovelCompletionCommand command = NovelRequestMapper.toUpdateNovelCompletionCommand(request, userId, novelPublicId);
        NovelCompletionResponse completionResponse = novelService.updateNovelCompletion(command);
        return ApiResponseMapper.success(completionResponse);
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
        DeleteNovelCommand command = NovelRequestMapper.toDeleteNovelCommand(userId, novelPublicId);
        novelService.deleteNovel(command);
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
