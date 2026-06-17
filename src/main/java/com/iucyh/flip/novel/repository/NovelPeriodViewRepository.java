package com.iucyh.flip.novel.repository;

import com.iucyh.flip.novel.domain.NovelPeriodStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NovelPeriodViewRepository extends JpaRepository<NovelPeriodStat, Long> {
}
