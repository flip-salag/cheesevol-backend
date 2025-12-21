package com.iucyh.novelservice.novel.web.dto.response;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;

public record NovelSummaryResponse(

        String novelId,
        UserBasicInfo author,
        String title,
        NovelCategory category,
        int totalViewCount,
        boolean isCompleted
) {}