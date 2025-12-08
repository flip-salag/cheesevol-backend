package com.iucyh.novelservice.novel.service.dto.command;

public record UpdateNovelCompletionCommand(

        long userId,
        String novelPublicId,
        boolean isCompleted
) {}
