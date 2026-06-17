package com.iucyh.novelservice.novel.web.dto.request;

import com.iucyh.novelservice.core.validator.enumfield.EnumField;
import com.iucyh.novelservice.core.validator.notblank.NotBlankIfPresent;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import jakarta.validation.constraints.Size;

import static com.iucyh.novelservice.novel.constant.NovelConstants.NOVEL_DESC_LENGTH_MAX;
import static com.iucyh.novelservice.novel.constant.NovelConstants.NOVEL_TITLE_LENGTH_MAX;
import static com.iucyh.novelservice.novel.constant.NovelConstants.NOVEL_TITLE_LENGTH_MIN;

public record UpdateNovelRequest(

        @NotBlankIfPresent
        @Size(min = NOVEL_TITLE_LENGTH_MIN, max = NOVEL_TITLE_LENGTH_MAX)
        String title,

        @Size(max = NOVEL_DESC_LENGTH_MAX)
        String description,

        @EnumField(enumClass = NovelCategory.class)
        String category
) {}
