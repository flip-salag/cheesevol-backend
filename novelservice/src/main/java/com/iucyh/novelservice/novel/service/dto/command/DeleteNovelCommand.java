package com.iucyh.novelservice.novel.service.dto.command;

public record DeleteNovelCommand(

        long userId,
        String novelPublicId
) {}
