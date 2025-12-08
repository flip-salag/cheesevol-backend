package com.iucyh.novelservice.novel.service.dto.command;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;

public record CreateNovelCommand(

        long userId,
        String title,
        String description,
        NovelCategory category
) {
    public CreateNovelCommand {
        title = title.strip();
    }
}
