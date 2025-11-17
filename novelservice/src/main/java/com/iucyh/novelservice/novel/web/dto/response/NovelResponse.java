package com.iucyh.novelservice.novel.web.dto.response;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.user.web.dto.response.UserSummaryResponse;

import java.time.LocalDateTime;

public record NovelResponse(

        String novelId,
        UserSummaryResponse author,
        String title,
        String description,
        NovelCategory category,
        int likeCount,
        int totalViewCount,
        boolean isCompleted,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {}
