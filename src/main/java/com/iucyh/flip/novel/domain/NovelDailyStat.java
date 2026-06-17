package com.iucyh.flip.novel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "novel_daily_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NovelDailyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_daily_stat_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDate statDate;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    public static NovelDailyStat of(LocalDate statDate, Novel novel) {
        NovelDailyStat stat = new NovelDailyStat();
        stat.statDate = statDate;
        stat.novel = novel;
        return stat;
    }
}
