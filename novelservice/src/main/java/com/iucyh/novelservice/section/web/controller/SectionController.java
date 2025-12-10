package com.iucyh.novelservice.section.web.controller;

import com.iucyh.novelservice.common.response.api.ApiResponseMapper;
import com.iucyh.novelservice.common.response.api.SuccessResponse;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.service.NovelQueryService;
import com.iucyh.novelservice.novel.web.dto.response.NovelResponse;
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

    private final NovelQueryService novelQueryService;

    @GetMapping("/popular")
    public SuccessResponse<List<NovelResponse>> getPopularNovelsForSection(
            @RequestParam(required = false) NovelCategory category
    ) {
        List<NovelResponse> result = novelQueryService.getPopularNovelsForSection(category);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/new")
    public SuccessResponse<List<NovelResponse>> getNewNovelsForSection() {
        List<NovelResponse> result = novelQueryService.getNewNovelsForSection();
        return ApiResponseMapper.success(result);
    }
}
