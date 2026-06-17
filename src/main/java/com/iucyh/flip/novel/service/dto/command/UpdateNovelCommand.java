package com.iucyh.flip.novel.service.dto.command;

import com.iucyh.flip.novel.enumtype.NovelCategory;

public record UpdateNovelCommand(

        long userId,
        String novelPublicId,
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
