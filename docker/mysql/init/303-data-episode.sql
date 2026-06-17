USE `flip`;

/*
==== 소설 당 회차 개수 ====
프롤로그가 존재하는 소설의 회차 개수 : 6개 (프롤로그 1개 + 일반 회차 5개)
프롤로그가 존재하지 않는 소설의 회차 개수 : 5개 (일반 회차 5개)

==== 회차 분포 ====
프롤로그가 존재하는 소설 개수 : 4개
프롤로그가 존재하지 않는 소설 개수(소설3, 소설4, 소설6) : 3개

삭제된 회차 개수(소설4, 소설5, 소설6의 회차번호 3번) : 3개 (+ 소설4, 소설8의 모든 회차들, 해당 소설들은 소설 자체가 삭제되었으므로 회차도 같이 삭제)

발행일 : 소설의 발행일 + 회차번호 + 2일 (각 회차마다 1일씩 간격을 줌)
생성일 : 소설의 생성일 + 회차번호 + 2일 (각 회차마다 1일씩 간격을 줌)
수정일 : 소설의 수정일 + 회차번호 + 2일 (각 회차마다 1일씩 간격을 줌)
*/

SET @novel1 := (SELECT novel_id FROM novel WHERE title = 'novel-1');
SET @novel3 := (SELECT novel_id FROM novel WHERE title = 'novel-3');
SET @novel4 := (SELECT novel_id FROM novel WHERE title = 'novel-4');
SET @novel5 := (SELECT novel_id FROM novel WHERE title = 'novel-5');
SET @novel6 := (SELECT novel_id FROM novel WHERE title = 'novel-6');
SET @novel7 := (SELECT novel_id FROM novel WHERE title = 'novel-7');
SET @novel8 := (SELECT novel_id FROM novel WHERE title = 'novel-8');

DROP TABLE IF EXISTS temp_episode_seq;
CREATE TABLE temp_episode_seq (ep_no INT NOT NULL);
INSERT INTO temp_episode_seq VALUES (0), (1), (2), (3), (4), (5);

INSERT INTO episode (novel_id, public_id, episode_type, title, description, content, episode_number, view_count, published_at, created_at, updated_at, deleted_at)
-- ==== novel 1 ====
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    REPEAT('a', 5000),
    e.ep_no,
    1000 + (e.ep_no * 5),
    DATE_ADD(n.published_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.created_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.updated_at, INTERVAL (e.ep_no + 2) DAY),
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
JOIN novel n ON n.novel_id = @novel1
UNION ALL
-- ==== novel 3 ====
SELECT
    n.novel_id,
    pid.public_id,
    'COMMON',
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    REPEAT('a', 5000),
    e.ep_no,
    1000 + (e.ep_no * 5),
    DATE_ADD(n.published_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.created_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.updated_at, INTERVAL (e.ep_no + 2) DAY),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 1 AS rn, 'XE1QLt6YC81sE0OxIZl7m' AS public_id
    UNION ALL
    SELECT 2, 'Vqc_98bLn5qnnfaFa9xQL'
    UNION ALL
    SELECT 3, 'ORpW9sbNGLlK_JNuyZwtL'
    UNION ALL
    SELECT 4, 'rRnc-uvFCZhyLL6jIWbv2'
    UNION ALL
    SELECT 5, '0BSj9fMtwJ2-vBQeeKXJY'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel3
WHERE e.ep_no > 0
UNION ALL
-- ==== novel 4 ====
SELECT
    n.novel_id,
    pid.public_id,
    'COMMON',
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    REPEAT('a', 5000),
    e.ep_no,
    1000 + (e.ep_no * 5),
    DATE_ADD(n.published_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.created_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.updated_at, INTERVAL (e.ep_no + 2) DAY),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 1 AS rn, 'IP10wNhIWZaCJ_nu8SIx0' AS public_id
    UNION ALL
    SELECT 2, 'hbR_sd6swYKWxAXR9plU-'
    UNION ALL
    SELECT 3, 'c4kcmv2KwIBCsNJri4VcB'
    UNION ALL
    SELECT 4, 'sTRpPSbwSKUzCKdmoLlel'
    UNION ALL
    SELECT 5, '2tenHAmhA9EIw37zif1eT'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel4
WHERE e.ep_no > 0
UNION ALL
-- ==== novel 5 ====
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    DATE_ADD(n.published_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.created_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.updated_at, INTERVAL (e.ep_no + 2) DAY),
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
JOIN novel n ON n.novel_id = @novel5
UNION ALL
-- ==== novel 6 ====
SELECT
    n.novel_id,
    pid.public_id,
    'COMMON',
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    DATE_ADD(n.published_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.created_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.updated_at, INTERVAL (e.ep_no + 2) DAY),
    n.deleted_at
FROM temp_episode_seq e
JOIN (
    SELECT 1 AS rn, 'IKzzxFamAP2pe8rmX9toR' AS public_id
    UNION ALL
    SELECT 2, 'O5O72NpyX0R99_Dv2pWZ6'
    UNION ALL
    SELECT 3, 'P19VyR-VdQ5j9X0YjK3m1'
    UNION ALL
    SELECT 4, 'SYq1Fg_CokacRUZQ5fDGx'
    UNION ALL
    SELECT 5, '1IfAlOrE9-njbno-NEiyG'
) pid ON pid.rn = e.ep_no
JOIN novel n ON n.novel_id = @novel6
WHERE e.ep_no > 0
UNION ALL
-- ==== novel 7 ====
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    DATE_ADD(n.published_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.created_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.updated_at, INTERVAL (e.ep_no + 2) DAY),
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
JOIN novel n ON n.novel_id = @novel7
UNION ALL
-- ==== novel 8 ====
SELECT
    n.novel_id,
    pid.public_id,
    CASE WHEN e.ep_no = 0 THEN 'PROLOGUE' ELSE 'COMMON' END,
    CONCAT(n.novel_id, '_episode_', e.ep_no),
    'dummy episode',
    CASE WHEN e.ep_no < 4 THEN REPEAT('a', 5000) ELSE REPEAT('a', 20000) END,
    e.ep_no,
    1000 + (e.ep_no * 5),
    DATE_ADD(n.published_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.created_at, INTERVAL (e.ep_no + 2) DAY),
    DATE_ADD(n.updated_at, INTERVAL (e.ep_no + 2) DAY),
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
UPDATE episode
SET deleted_at = CURRENT_TIMESTAMP
WHERE episode_number = 3 AND novel_id IN (@novel4, @novel5, @novel6);

UPDATE novel n
JOIN episode e ON e.novel_id = n.novel_id AND e.episode_number = 5
SET
    common_episode_count = 5,
    max_episode_number = 5,
    last_episode_publish_date = DATE(e.published_at);

DROP TABLE IF EXISTS temp_episode_seq;