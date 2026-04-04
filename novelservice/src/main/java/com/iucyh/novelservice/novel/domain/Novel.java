package com.iucyh.novelservice.novel.domain;

import com.iucyh.novelservice.base.entity.PublicEntity;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.iucyh.novelservice.novel.constant.NovelConstants.NOVEL_DESC_LENGTH_MAX;
import static com.iucyh.novelservice.novel.constant.NovelConstants.NOVEL_TITLE_LENGTH_MAX;

@Entity
@Table(name = "novel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Novel extends PublicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long id;

    @Version
    private Long version;

    @Column(length = NOVEL_TITLE_LENGTH_MAX, nullable = false)
    private String title;

    @Column(length = NOVEL_DESC_LENGTH_MAX, nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private NovelCategory category;

    @Column(nullable = false)
    private Integer commonEpisodeCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer totalViewCount = 0;

    @Column(nullable = false)
    private Integer periodViewCount = 0;

    @Column(nullable = false)
    private Integer maxEpisodeNumber = 0;

    private LocalDate lastEpisodePublishDate;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Novel of(String title, String description, NovelCategory category, LocalDateTime publishedAt, User user) {
        Novel novel = new Novel();
        novel.title = title.strip();
        novel.description = description;
        novel.category = category;
        novel.publishedAt = publishedAt;
        novel.user = user;
        return novel;
    }

    public boolean isCompletedNovel() {
        return isCompleted;
    }

    public int generateNewEpisodeNumber() {
        return maxEpisodeNumber + 1;
    }

    public void increaseCommonEpisodeCount() {
        commonEpisodeCount++;
    }

    public void decreaseCommonEpisodeCount() {
        if (commonEpisodeCount > 0) {
            commonEpisodeCount--;
        }
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
        if (!this.isCompleted.equals(isCompleted)) {
            this.isCompleted = isCompleted;
        }
    }

    public void updateMaxEpisodeNumber(Integer maxEpisodeNumber) {
        // 기존값보다 인자의 값이 클때만 업데이트(max_episode_number는 절대 감소하지 않는 컬럼)
        if (maxEpisodeNumber != null && maxEpisodeNumber > this.maxEpisodeNumber) {
            this.maxEpisodeNumber = maxEpisodeNumber;
        }
    }

    public void updateLastEpisodePublishDate(LocalDate lastEpisodePublishDate) {
        this.lastEpisodePublishDate = lastEpisodePublishDate;
    }
}
