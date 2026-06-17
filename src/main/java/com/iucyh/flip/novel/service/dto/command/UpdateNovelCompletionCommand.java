package com.iucyh.flip.novel.service.dto.command;

public record UpdateNovelCompletionCommand(

        long userId,
        String novelPublicId,
        boolean isCompleted
) {}
