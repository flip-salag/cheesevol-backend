package com.iucyh.novelservice.section.web.controller;

import com.iucyh.novelservice.common.response.api.ApiResponseMapper;
import com.iucyh.novelservice.common.response.api.SuccessResponse;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.web.dto.response.NovelSummaryResponse;
import com.iucyh.novelservice.section.service.SectionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionQueryService sectionQueryService;

    @GetMapping("/popular")
    public SuccessResponse<List<NovelSummaryResponse>> getPopularNovels(
            @RequestParam(required = false) NovelCategory category
    ) {
        List<NovelSummaryResponse> result = sectionQueryService.getPopularNovels(category);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/new")
    public SuccessResponse<List<NovelSummaryResponse>> getNewNovels() {
        List<NovelSummaryResponse> result = sectionQueryService.getNewNovels();
        return ApiResponseMapper.success(result);
    }
}
