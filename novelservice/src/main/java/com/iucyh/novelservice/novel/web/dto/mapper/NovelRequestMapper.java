package com.iucyh.novelservice.novel.web.dto.mapper;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.novelservice.novel.web.dto.request.CreateNovelRequest;

public class NovelRequestMapper {

    private NovelRequestMapper() {}

    public static CreateNovelCommand toCreateNovelCommand(CreateNovelRequest request) {
        return new CreateNovelCommand(
                request.title(),
                request.description(),
                NovelCategory.of(request.category())
        );
    }
}
