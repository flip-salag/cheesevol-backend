package com.iucyh.novelservice.novel.web.dto.mapper;

import com.iucyh.novelservice.common.response.PageWithCursorResponse;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelSummaryResponse;
import com.iucyh.novelservice.user.domain.User;
import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;

import java.util.List;

public class NovelResponseMapper {

    private NovelResponseMapper() {}

    public static NovelSummaryResponse toNovelSummaryResponse(Novel novel) {
        User user = novel.getUser();
        UserBasicInfo author = new UserBasicInfo(user.getPublicId(), user.getNickname());

        return new NovelSummaryResponse(
                novel.getPublicId(),
                author,
                novel.getTitle(),
                novel.getDescription(),
                novel.getCategory(),
                novel.getLikeCount(),
                novel.getTotalViewCount(),
                novel.getIsCompleted(),
                novel.getUpdatedAt(),
                novel.getCreatedAt()
        );
    }

    public static NovelCompletionResponse toNovelCompletionResponse(Novel novel) {
        return new NovelCompletionResponse(
                novel.getPublicId(),
                novel.getIsCompleted()
        );
    }

    public static NovelLikeCountResponse toNovelLikeCountResponse(int likeCount) {
        return new NovelLikeCountResponse(likeCount);
    }

    public static PageWithCursorResponse<NovelSummaryResponse> toPageResponse(List<NovelSummaryResponse> novels, String encodedCursor, int size) {
        return new PageWithCursorResponse<>(size, encodedCursor, novels);
    }
}
