package com.iucyh.novelservice.novel.repository.query.projection;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NovelSimpleQueryProjection {

    private final String publicId;
    private final String userPublicId;
    private final String userNickname;
    private final String title;
    private final String description;
    private final NovelCategory category;
    private final Integer likeCount;
    private final Integer totalViewCount;
    private final Boolean isCompleted;
    private final LocalDateTime updatedAt;
    private final LocalDateTime createdAt;

    @QueryProjection
    public NovelSimpleQueryProjection(
            String publicId,
            String userPublicId, String userNickname,
            String title, String description,
            NovelCategory category,
            Integer likeCount, Integer totalViewCount,
            Boolean isCompleted,
            LocalDateTime updatedAt, LocalDateTime createdAt
    ) {
        this.publicId = publicId;
        this.userPublicId = userPublicId;
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
