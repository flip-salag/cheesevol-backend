package com.iucyh.novelservice.novel.web.dto.mapper;

import com.iucyh.novelservice.common.response.PageWithCursorResponse;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelDetailResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelSaveResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelSummaryResponse;
import com.iucyh.novelservice.user.domain.User;
import com.iucyh.novelservice.user.web.dto.response.info.UserBasicInfo;

import java.util.List;

public class NovelResponseMapper {

    private NovelResponseMapper() {}

    public static NovelSaveResponse toNovelSaveResponse(Novel novel) {
        return new NovelSaveResponse(
                novel.getPublicId(),
                novel.getUpdatedAt(),
                novel.getCreatedAt()
        );
    }

    public static NovelSummaryResponse toNovelSummaryResponse(Novel novel) {
        User user = novel.getUser();
        UserBasicInfo author = new UserBasicInfo(user.getPublicId(), user.getNickname());

        return new NovelSummaryResponse(
                novel.getPublicId(),
                author,
                novel.getTitle(),
                novel.getCategory(),
                novel.getTotalViewCount(),
                novel.getIsCompleted()
        );
    }

    public static NovelDetailResponse toNovelDetailResponse(Novel novel) {
        User user = novel.getUser();
        UserBasicInfo author = new UserBasicInfo(user.getPublicId(), user.getNickname());

        return new NovelDetailResponse(
                novel.getPublicId(),
                novel.getTitle(),
                novel.getDescription(),
                novel.getCategory(),
                novel.getCommonEpisodeCount(),
                novel.getLikeCount(),
                novel.getTotalViewCount(),
                novel.isCompletedNovel(),
                novel.getPublishedAt(),
                author
        );
    }

    public static NovelCompletionResponse toNovelCompletionResponse(Novel novel) {
        return new NovelCompletionResponse(
                novel.getPublicId(),
                novel.getIsCompleted(),
                novel.getUpdatedAt(),
                novel.getCreatedAt()
        );
    }

    public static NovelLikeCountResponse toNovelLikeCountResponse(int likeCount) {
        return new NovelLikeCountResponse(likeCount);
    }

    public static PageWithCursorResponse<NovelSummaryResponse> toPageResponse(List<Novel> novels, String encodedCursor, int limit) {
        List<NovelSummaryResponse> result = novels.stream()
                .map(NovelResponseMapper::toNovelSummaryResponse)
                .toList();
        return new PageWithCursorResponse<>(limit, encodedCursor, result);
    }
}
