package com.iucyh.flip.novel.service.dto.command;

import com.iucyh.flip.novel.enumtype.NovelCategory;

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
