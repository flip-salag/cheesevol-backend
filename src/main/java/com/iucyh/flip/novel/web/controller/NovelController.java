package com.iucyh.flip.novel.web.controller;

import com.iucyh.flip.base.response.api.ApiResponseMapper;
import com.iucyh.flip.base.response.api.SuccessResponse;
import com.iucyh.flip.common.response.PageWithCursorResponse;
import com.iucyh.flip.novel.enumtype.NovelCategory;
import com.iucyh.flip.novel.service.NovelQueryService;
import com.iucyh.flip.novel.service.NovelService;
import com.iucyh.flip.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.flip.novel.service.dto.command.DeleteNovelCommand;
import com.iucyh.flip.novel.service.dto.command.UpdateNovelCommand;
import com.iucyh.flip.novel.service.dto.command.UpdateNovelCompletionCommand;
import com.iucyh.flip.novel.service.dto.query.GetNewNovelsQuery;
import com.iucyh.flip.novel.service.dto.query.GetNovelsQuery;
import com.iucyh.flip.novel.web.dto.mapper.NovelRequestMapper;
import com.iucyh.flip.novel.web.dto.request.CreateNovelRequest;
import com.iucyh.flip.novel.web.dto.request.NovelPageRequest;
import com.iucyh.flip.novel.web.dto.request.UpdateNovelCompletionRequest;
import com.iucyh.flip.novel.web.dto.request.UpdateNovelRequest;
import com.iucyh.flip.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.flip.novel.web.dto.response.NovelDetailResponse;
import com.iucyh.flip.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.flip.novel.web.dto.response.NovelSaveResponse;
import com.iucyh.flip.novel.web.dto.response.NovelSummaryResponse;
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
    private final NovelQueryService novelQueryService;

    @GetMapping
    public SuccessResponse<PageWithCursorResponse<NovelSummaryResponse>> getNovels(
            @Valid @ModelAttribute NovelPageRequest request,
            @RequestParam(required = false) NovelCategory category
    ) {
        GetNovelsQuery query = NovelRequestMapper.toGetNovelsQuery(request, category);
        PageWithCursorResponse<NovelSummaryResponse> result = novelQueryService.getNovels(query);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/new")
    public SuccessResponse<PageWithCursorResponse<NovelSummaryResponse>> getNewNovels(
            @Valid @ModelAttribute NovelPageRequest request,
            @RequestParam(required = false) NovelCategory category
    ) {
        GetNewNovelsQuery query = NovelRequestMapper.toGetNewNovelsQuery(request, category);
        PageWithCursorResponse<NovelSummaryResponse> result = novelQueryService.getNewNovels(query);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/{novelPublicId}")
    public SuccessResponse<NovelDetailResponse> getNovelDetail(
            @PathVariable String novelPublicId
    ) {
        NovelDetailResponse result = novelQueryService.getNovelDetail(novelPublicId);
        return ApiResponseMapper.success(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<NovelSaveResponse> createNovel(
            @Valid @RequestBody CreateNovelRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        CreateNovelCommand command = NovelRequestMapper.toCreateNovelCommand(request, userId);
        NovelSaveResponse result = novelService.createNovel(command);
        return ApiResponseMapper.success(result);
    }

    @PatchMapping("/{novelPublicId}")
    public SuccessResponse<NovelSaveResponse> updateNovel(
            @PathVariable String novelPublicId,
            @Valid @RequestBody UpdateNovelRequest request,
            @RequestHeader(TEMP_USER_ID_HEADER) long userId
    ) {
        UpdateNovelCommand command = NovelRequestMapper.toUpdateNovelCommand(request, userId, novelPublicId);
        NovelSaveResponse result = novelService.updateNovel(command);
        return ApiResponseMapper.success(result);
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
}
