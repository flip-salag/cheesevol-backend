package com.iucyh.novelservice.novel.repository.query.projection;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NovelSimpleQueryProjection {

    private final String publicId;
    private final Long userId;
    private final String userNickname;
    private final String title;
    private final String description;
    private final NovelCategory category;
    private final Integer likeCount;
    private final Integer totalViewCount;
    private final Boolean isCompleted;
    private final Long updatedAt;
    private final Long createdAt;

    @QueryProjection
    public NovelSimpleQueryProjection(
            String publicId,
            Long userId, String userNickname,
            String title, String description,
            NovelCategory category,
            Integer likeCount, Integer totalViewCount,
            Boolean isCompleted,
            Long updatedAt, Long createdAt
    ) {
        this.publicId = publicId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.title = title;
        this.description = description;
        this.category = category;
        this.likeCount = likeCount;
        this.totalViewCount = totalViewCount;
        this.isCompleted = isCompleted;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
