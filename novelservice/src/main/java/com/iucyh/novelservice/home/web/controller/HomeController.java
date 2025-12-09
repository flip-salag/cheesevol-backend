package com.iucyh.novelservice.home.web.controller;

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
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final NovelQueryService novelQueryService;

    @GetMapping("/sections/novels")
    public SuccessResponse<List<NovelResponse>> getNovels(
            @RequestParam NovelCategory category
    ) {
        List<NovelResponse> result = novelQueryService.getNovelsForSection(category);
        return ApiResponseMapper.success(result);
    }

    @GetMapping("/sections/new-novels")
    public SuccessResponse<List<NovelResponse>> getNewNovels() {
        List<NovelResponse> result = novelQueryService.getNewNovelsForSection();
        return ApiResponseMapper.success(result);
    }
}
