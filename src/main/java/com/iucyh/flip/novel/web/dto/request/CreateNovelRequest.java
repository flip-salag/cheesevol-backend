package com.iucyh.flip.novel.web.dto.request;

import com.iucyh.flip.core.validator.enumfield.EnumField;
import com.iucyh.flip.novel.enumtype.NovelCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.iucyh.flip.novel.constant.NovelConstants.NOVEL_DESC_LENGTH_MAX;
import static com.iucyh.flip.novel.constant.NovelConstants.NOVEL_TITLE_LENGTH_MAX;
import static com.iucyh.flip.novel.constant.NovelConstants.NOVEL_TITLE_LENGTH_MIN;

public record CreateNovelRequest(

        @NotBlank
        @Size(min = NOVEL_TITLE_LENGTH_MIN, max = NOVEL_TITLE_LENGTH_MAX)
        String title,

        @NotNull
        @Size(max = NOVEL_DESC_LENGTH_MAX)
        String description,

        @NotNull
        @EnumField(enumClass = NovelCategory.class)
        String category
) {}
