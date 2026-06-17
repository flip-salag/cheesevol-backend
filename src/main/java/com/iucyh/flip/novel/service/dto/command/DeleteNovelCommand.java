package com.iucyh.flip.novel.service.dto.command;

public record DeleteNovelCommand(

        long userId,
        String novelPublicId
) {}
