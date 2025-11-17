package com.iucyh.novelservice.novel.service.command;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;

public record UpdateNovelCommand(

        String title,
        String description,
        NovelCategory category
) {
    public UpdateNovelCommand {
        if (title != null) {
            title = title.strip();
        }
    }
}
