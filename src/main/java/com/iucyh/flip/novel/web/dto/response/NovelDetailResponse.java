package com.iucyh.flip.novel.web.dto.response;

import com.iucyh.flip.novel.enumtype.NovelCategory;
import com.iucyh.flip.user.web.dto.response.info.UserBasicInfo;

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
