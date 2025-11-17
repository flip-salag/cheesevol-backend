package com.iucyh.novelservice.novel.service.command;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;

public record CreateNovelCommand(

        String title,
        String description,
        NovelCategory category
) {
    public CreateNovelCommand {
        title = title.strip();
    }
}
