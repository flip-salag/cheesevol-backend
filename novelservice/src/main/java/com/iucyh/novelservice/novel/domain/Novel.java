package com.iucyh.novelservice.novel.domain;

import com.iucyh.novelservice.base.entity.PublicEntity;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.iucyh.novelservice.novel.constant.NovelConstants.*;

@Entity
@Table(name = "novel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Novel extends PublicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long id;

    @Column(length = NOVEL_TITLE_LENGTH_MAX, nullable = false)
    private String title;

    @Column(length = NOVEL_DESC_LENGTH_MAX, nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private NovelCategory category;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer totalViewCount = 0;

    @Column(nullable = false)
    private Integer periodViewCount = 0;

    @Column(nullable = false)
    private Integer lastEpisodeNumber = 0;

    private LocalDateTime lastPublishedAt;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Novel of(String title, String description, NovelCategory category, User user) {
        Novel novel = new Novel();
        novel.title = title.strip();
        novel.description = description;
        novel.category = category;
        novel.user = user;
        return novel;
    }

    public boolean isCompletedNovel() {
        return isCompleted;
    }

    public void updateTextMetaData(String title, String description) {
        if (title != null) {
            this.title = title.strip();
        }

        if (description != null) {
            this.description = description;
        }
    }

    public void updateCategory(NovelCategory category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void updateCompletion(Boolean isCompleted) {
        if (this.isCompleted != isCompleted) {
            this.isCompleted = isCompleted;
        }
    }

    public void updateLastEpisode(Integer lastEpisodeNumber, LocalDateTime lastPublishedAt) {
        this.lastEpisodeNumber = lastEpisodeNumber;
        this.lastPublishedAt = lastPublishedAt;
    }

    public void updateLastPublishedAt(LocalDateTime lastPublishedAt) {
        this.lastPublishedAt = lastPublishedAt;
    }
}
