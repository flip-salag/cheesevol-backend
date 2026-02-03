package com.iucyh.novelservice.novel.web.dto.response;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;

import java.time.LocalDateTime;

public record NovelDetailResponse(

        String novelId,
        String title,
        String description,
        NovelCategory category,
        int commonEpisodeCount,
        int likeCount,
        int totalViewCount,
        boolean isCompleted,
        LocalDateTime publishedAt,
        UserBasicInfo author
) {}
