package com.iucyh.flip.episode.domain;

import com.iucyh.flip.base.entity.PublicEntity;
import com.iucyh.flip.episode.enumtype.EpisodeType;
import com.iucyh.flip.novel.domain.Novel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_DESC_LENGTH_MAX;
import static com.iucyh.flip.episode.constant.EpisodeConstants.EPISODE_TITLE_LENGTH_MAX;

@Entity
@Table(name = "episode")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Episode extends PublicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "episode_id")
    private Long id;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private EpisodeType episodeType;

    @Column(length = EPISODE_TITLE_LENGTH_MAX, nullable = false)
    private String title;

    @Column(length = EPISODE_DESC_LENGTH_MAX, nullable = false)
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    public static Episode of(EpisodeType episodeType, String title, String description, String content, Integer episodeNumber, LocalDateTime publishedAt, Novel novel) {
        Episode episode = new Episode();
        episode.episodeType = episodeType;
        episode.title = title.strip();
        episode.description = description;
        episode.content = content;
        episode.episodeNumber = episodeNumber;
        episode.publishedAt = publishedAt;
        episode.novel = novel;
        return episode;
    }

    public void updateTextMetaData(String title, String description) {
        if (title != null) {
            this.title = title.strip();
        }

        if (description != null) {
            this.description = description;
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
