USE novel_service;

-- ==== novel ====
CREATE INDEX idx_novel_sort_popular ON novel (deleted_at, period_view_count DESC, last_episode_publish_date DESC, novel_id DESC);
CREATE INDEX idx_novel_sort_last_update ON novel (deleted_at, last_episode_publish_date DESC, total_view_count DESC, novel_id DESC);
CREATE INDEX idx_novel_sort_view_count ON novel (deleted_at, total_view_count DESC, last_episode_publish_date DESC, novel_id DESC);
CREATE INDEX idx_novel_sort_like_count ON novel (deleted_at, like_count DESC, total_view_count DESC, novel_id DESC);

-- with category (일단 기본값으로 쓰이는 인기순에 대해서만 생성)
CREATE INDEX idx_novel_sort_popular_category ON novel (deleted_at, category, period_view_count DESC, last_episode_publish_date DESC, novel_id DESC);