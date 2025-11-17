package com.iucyh.novelservice.novel.web.dto.response;

public record NovelCompletionResponse(

        String novelId,
        boolean isCompleted
) {}
