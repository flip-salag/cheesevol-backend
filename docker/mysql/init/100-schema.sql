USE flip;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    user_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id  VARCHAR(25)  NOT NULL,
    email      VARCHAR(320) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    nickname   VARCHAR(20)  NOT NULL,
    bio        VARCHAR(100) NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    deleted_at DATETIME NULL,

    UNIQUE KEY uq_user_public_id (public_id),
    UNIQUE KEY uq_user_email (email)
);

DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel`
(
    novel_id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                   BIGINT       NOT NULL,
    public_id                 VARCHAR(25)  NOT NULL,
    version                   BIGINT       NOT NULL DEFAULT 0,
    title                     VARCHAR(50)  NOT NULL,
    description               VARCHAR(500) NOT NULL,
    category                  VARCHAR(50)  NOT NULL,
    common_episode_count      INT          NOT NULL DEFAULT 0,
    like_count                INT          NOT NULL DEFAULT 0,
    total_view_count          INT          NOT NULL DEFAULT 0,
    period_view_count         INT          NOT NULL DEFAULT 0,
    max_episode_number        INT          NOT NULL DEFAULT 0,
    last_episode_publish_date DATE NULL,
    is_completed              BOOLEAN      NOT NULL,
    published_at              DATETIME     NOT NULL,
    created_at                DATETIME     NOT NULL,
    updated_at                DATETIME     NOT NULL,
    deleted_at                DATETIME NULL,

    CONSTRAINT fk_novel_user FOREIGN KEY (user_id) REFERENCES user (user_id),
    UNIQUE KEY uq_novel_public_id (public_id),
    UNIQUE KEY uq_novel_user_id_title (user_id, title)
);

DROP TABLE IF EXISTS `episode`;
CREATE TABLE `episode`
(
    episode_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id       BIGINT      NOT NULL,
    public_id      VARCHAR(25) NOT NULL,
    episode_type   VARCHAR(50) NOT NULL,
    title          VARCHAR(50) NOT NULL,
    description    VARCHAR(35) NOT NULL,
    content        TEXT        NOT NULL,
    episode_number INT         NOT NULL,
    view_count     INT         NOT NULL DEFAULT 0,
    published_at   DATETIME    NOT NULL,
    created_at     DATETIME    NOT NULL,
    updated_at     DATETIME    NOT NULL,
    deleted_at     DATETIME NULL,

    CONSTRAINT fk_episode_novel FOREIGN KEY (novel_id) REFERENCES novel (novel_id),
    UNIQUE KEY uq_episode_public_id (public_id),
    UNIQUE KEY uq_episode_novel_id_episode_number (novel_id, episode_number)
);

DROP TABLE IF EXISTS `novel_like`;
CREATE TABLE `novel_like`
(
    novel_like_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT   NOT NULL,
    novel_id      BIGINT   NOT NULL,
    created_at    DATETIME NOT NULL,

    CONSTRAINT fk_novel_like_user FOREIGN KEY (user_id) REFERENCES user (user_id),
    CONSTRAINT fk_novel_like_novel FOREIGN KEY (novel_id) REFERENCES novel (novel_id),
    UNIQUE KEY uq_novel_like_user_id_novel_id (user_id, novel_id)
);

DROP TABLE IF EXISTS `novel_daily_stats`;
CREATE TABLE `novel_daily_stats`
(
    novel_daily_stat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id            BIGINT NOT NULL,
    stat_date           DATE   NOT NULL,
    view_count          INT    NOT NULL DEFAULT 0,

    CONSTRAINT fk_novel_daily_stats_novel FOREIGN KEY (novel_id) REFERENCES novel (novel_id),
    UNIQUE KEY uq_novel_daily_stats_novel_id_stat_date (novel_id, stat_date)
);

DROP TABLE IF EXISTS `novel_period_stats`;
CREATE TABLE `novel_period_stats`
(
    novel_period_stat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id             BIGINT      NOT NULL,
    period_type          VARCHAR(50) NOT NULL,
    start_date           DATE        NOT NULL,
    end_date             DATE        NOT NULL,
    view_count           INT         NOT NULL DEFAULT 0,
    updated_at           DATETIME    NOT NULL,

    CONSTRAINT fk_novel_period_stats_novel FOREIGN KEY (novel_id) REFERENCES novel (novel_id),
    UNIQUE KEY uq_novel_period_stats_novel_id_period_type_start_date_end_date (novel_id, period_type, start_date, end_date)
);