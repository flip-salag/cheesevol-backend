package com.iucyh.flip.section.web.controller;

import com.iucyh.flip.base.response.api.ApiResponseMapper;
import com.iucyh.flip.base.response.api.SuccessResponse;
import com.iucyh.flip.novel.enumtype.NovelCategory;
import com.iucyh.flip.novel.web.dto.response.NovelSummaryResponse;
import com.iucyh.flip.section.service.SectionQueryService;
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
