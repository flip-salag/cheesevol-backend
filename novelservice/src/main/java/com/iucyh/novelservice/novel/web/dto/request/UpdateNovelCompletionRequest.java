package com.iucyh.novelservice.novel.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateNovelCompletionRequest(

        @NotNull
        Boolean isCompleted
) {}