USE `novel_service`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id  VARCHAR(25)  NOT NULL UNIQUE,
    email      VARCHAR(320) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    nickname   VARCHAR(20)  NOT NULL,
    bio        VARCHAR(100) NOT NULL DEFAULT '',
    created_at DATETIME     NOT NULL DEFAULT NOW(),
    updated_at DATETIME     NOT NULL DEFAULT NOW(),
    deleted_at DATETIME NULL
);

DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel`
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT       NOT NULL,
    public_id           VARCHAR(25)  NOT NULL UNIQUE,
    title               VARCHAR(55)  NOT NULL,
    description         VARCHAR(505) NOT NULL DEFAULT '',
    category            VARCHAR(50)  NOT NULL,
    like_count          INT          NOT NULL DEFAULT 0,
    total_view_count    INT          NOT NULL DEFAULT 0,
    period_view_count   INT          NOT NULL DEFAULT 0,
    last_episode_number INT          NOT NULL DEFAULT 0,
    last_episode_at     DATETIME NULL,
    is_completed        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at          DATETIME     NOT NULL DEFAULT NOW(),
    updated_at          DATETIME     NOT NULL DEFAULT NOW(),
    deleted_at          DATETIME NULL,

    CONSTRAINT fk_novel_user FOREIGN KEY (user_id) REFERENCES user (id),
    UNIQUE KEY uq_user_title (user_id, title)
);

DROP TABLE IF EXISTS `episode`;
CREATE TABLE `episode`
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id       BIGINT      NOT NULL,
    public_id      VARCHAR(25) NOT NULL UNIQUE,
    episode_type   VARCHAR(50) NOT NULL,
    title          VARCHAR(55) NOT NULL,
    description    VARCHAR(40) NOT NULL DEFAULT '',
    content        TEXT        NOT NULL,
    episode_number INT         NOT NULL,
    view_count     INT         NOT NULL DEFAULT 0,
    created_at     DATETIME    NOT NULL DEFAULT NOW(),
    updated_at     DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at     DATETIME NULL,

    CONSTRAINT fk_episode_novel FOREIGN KEY (novel_id) REFERENCES novel (id),
    UNIQUE KEY uq_novel_episode_number (novel_id, episode_number)
);

DROP TABLE IF EXISTS `novel_like`;
CREATE TABLE `novel_like`
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    novel_id   BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_novel_like_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_novel_like_novel FOREIGN KEY (novel_id) REFERENCES novel (id),
    UNIQUE KEY uq_user_novel (user_id, novel_id)
);

DROP TABLE IF EXISTS `novel_daily_stats`;
CREATE TABLE `novel_daily_stats`
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id   BIGINT NOT NULL,
    stat_date  DATE   NOT NULL,
    view_count INT    NOT NULL DEFAULT 0,

    CONSTRAINT fk_novel_daily_stat_novel FOREIGN KEY (novel_id) REFERENCES novel (id),
    UNIQUE KEY uq_novel_stat_date (novel_id, stat_date)
);

DROP TABLE IF EXISTS `novel_period_stats`;
CREATE TABLE `novel_period_stats`
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id    BIGINT      NOT NULL,
    period_type VARCHAR(50) NOT NULL,
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    view_count  INT         NOT NULL DEFAULT 0,
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_novel_period_stat_novel FOREIGN KEY (novel_id) REFERENCES novel (id),
    UNIQUE KEY uq_novel_start_end (novel_id, period_type, start_date, end_date)
);