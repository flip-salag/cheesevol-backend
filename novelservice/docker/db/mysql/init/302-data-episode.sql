USE `novel_service`;

SET @novel1 := (SELECT novel_id FROM novel WHERE title = 'novel-1');
SET @novel2 := (SELECT novel_id FROM novel WHERE title = 'novel-2');
SET @novel3 := (SELECT novel_id FROM novel WHERE title = 'novel-3');
SET @novel4 := (SELECT novel_id FROM novel WHERE title = 'novel-4');
SET @novel5 := (SELECT novel_id FROM novel WHERE title = 'novel-5');
SET @novel6 := (SELECT novel_id FROM novel WHERE title = 'novel-6');
SET @novel7 := (SELECT novel_id FROM novel WHERE title = 'novel-7');
SET @novel8 := (SELECT novel_id FROM novel WHERE title = 'novel-8');

DROP TEMPORARY TABLE IF EXISTS temp_episode_seq;
CREATE TEMPORARY TABLE temp_episode_seq (ep_no INT NOT NULL);
INSERT INTO temp_episode_seq VALUES (0), (1), (2), (3), (4), (5);

-- ==== novel 1 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    REPEAT('a', 5000),
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, 'p7MTUQZhSddTze_cAHIRc' AS public_id
    UNION ALL
    SELECT 1, 'GxiEiRvQJWzgiNVoMFX7q'
    UNION ALL
    SELECT 2, 'GYUdV7rEteBlTeT9jXTm3'
    UNION ALL
    SELECT 3, 'hLLNedZeghgY_eEiLB_3U'
    UNION ALL
    SELECT 4, 'ZGUlk397hHdSzZAiPvwS_'
    UNION ALL
    SELECT 5, '9VgtrkaKNZC5vk9rvQtfO'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel1;

-- ==== novel 2 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    REPEAT('a', 5000),
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, 'InD3qvD_naBdY6BLgtJyo' AS public_id
    UNION ALL
    SELECT 1, 'Nn8vqEq8VNezIAE11FZ_C'
    UNION ALL
    SELECT 2, 'vR1-QANWW1QvxvfM1e81U'
    UNION ALL
    SELECT 3, '2XZBM8vDapnSKFMGQqD4w'
    UNION ALL
    SELECT 4, 'MYPWuzHzlREht_lYIylvt'
    UNION ALL
    SELECT 5, 'D0R5hFC5y4CZeqcy5v8TB'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel2;

-- ==== novel 3 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    REPEAT('a', 5000),
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, 'cJkfEXh4Av0SKG_uHMi3_' AS public_id
    UNION ALL
    SELECT 1, 'XE1QLt6YC81sE0OxIZl7m'
    UNION ALL
    SELECT 2, 'Vqc_98bLn5qnnfaFa9xQL'
    UNION ALL
    SELECT 3, 'ORpW9sbNGLlK_JNuyZwtL'
    UNION ALL
    SELECT 4, 'rRnc-uvFCZhyLL6jIWbv2'
    UNION ALL
    SELECT 5, '0BSj9fMtwJ2-vBQeeKXJY'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel3;

-- ==== novel 4 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    REPEAT('a', 5000),
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, '-B6HzHQ-1ceMSz_NFfuBz' AS public_id
    UNION ALL
    SELECT 1, 'IP10wNhIWZaCJ_nu8SIx0'
    UNION ALL
    SELECT 2, 'hbR_sd6swYKWxAXR9plU-'
    UNION ALL
    SELECT 3, 'c4kcmv2KwIBCsNJri4VcB'
    UNION ALL
    SELECT 4, 'sTRpPSbwSKUzCKdmoLlel'
    UNION ALL
    SELECT 5, '2tenHAmhA9EIw37zif1eT'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel4;

-- ==== novel 5 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, '_14uTv3cxU-1gI-T4wkKe' AS public_id
    UNION ALL
    SELECT 1, 'nfQK-l1_sJoVZdN9YNm_l'
    UNION ALL
    SELECT 2, 'RkW3hCv6D7HI2p03GEGFW'
    UNION ALL
    SELECT 3, 'mydCvKtcrrK1b9TgEmFwH'
    UNION ALL
    SELECT 4, 'VGkDAeFx00vr1XgRK2fn_'
    UNION ALL
    SELECT 5, 'LomE6_voKK3t-hz6Xe_C-'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel5;

-- ==== novel 6 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, 'fSmdtM3QEzCIJRvHZjkmA' AS public_id
    UNION ALL
    SELECT 1, 'IKzzxFamAP2pe8rmX9toR'
    UNION ALL
    SELECT 2, 'O5O72NpyX0R99_Dv2pWZ6'
    UNION ALL
    SELECT 3, 'P19VyR-VdQ5j9X0YjK3m1'
    UNION ALL
    SELECT 4, 'SYq1Fg_CokacRUZQ5fDGx'
    UNION ALL
    SELECT 5, '1IfAlOrE9-njbno-NEiyG'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel6;

-- ==== novel 7 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, '4CpwYGFTqlgbugfDaeHa7' AS public_id
    UNION ALL
    SELECT 1, '3vdWDzu-m2x6cWLvw9dwc'
    UNION ALL
    SELECT 2, 'Sj4bKZmL-Hm-fRXqaNMxp'
    UNION ALL
    SELECT 3, 'OTw0FACcOjZ-8w6I-_PyX'
    UNION ALL
    SELECT 4, '0e3R6sfyYLbDrI8KlTRN4'
    UNION ALL
    SELECT 5, '8Jmhkd4Z1LUKDrFKbXCkj'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel7;

-- ==== novel 8 ====
INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, deleted_at)
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 0 AS rn, 'yFb4x48RMHhgVKs4szpM9' AS public_id
    UNION ALL
    SELECT 1, '-OVCBz0dfgeOjg-ZpjzUo'
    UNION ALL
    SELECT 2, 'fQi9PVSYDrrstXhHjNjfP'
    UNION ALL
    SELECT 3, 'o3QtYKOU_twpz9-tStDzA'
    UNION ALL
    SELECT 4, 'Hxeozx0rdoPwyHTfubAMg'
    UNION ALL
    SELECT 5, 'fa_K9lvdnYEK1WjaW-wBx'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel8;

-- ==== after insert ====
UPDATE episode e
JOIN novel n ON n.novel_id = e.novel_id
SET
    e.created_at = DATE_ADD(n.created_at, INTERVAL (e.episode_number + 2) DAY),
    e.updated_at = DATE_ADD(n.created_at, INTERVAL (e.episode_number + 2) DAY);

UPDATE episode
SET deleted_at = NOW()
WHERE episode_number = 3 AND novel_id IN (@novel4, @novel5);

DROP TEMPORARY TABLE IF EXISTS temp_episode_seq;