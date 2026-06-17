package com.iucyh.flip.novel.web.dto.response;

import com.iucyh.flip.novel.enumtype.NovelCategory;
import com.iucyh.flip.user.web.dto.response.info.UserBasicInfo;

public record NovelSummaryResponse(

        String novelId,
        UserBasicInfo author,
        String title,
        NovelCategory category,
        int totalViewCount,
        boolean isCompleted
) {}