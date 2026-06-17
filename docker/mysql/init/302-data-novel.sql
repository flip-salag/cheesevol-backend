USE `novel_service`;

/*
==== 작가 당 소설 개수 ====
writer1 : 4개
writer2 : 6개

==== 소설 분포 ====
삭제되지 않은 소설 개수 : 7개
삭제된 소설 개수(소설4, 소설8, 소설9) : 3개

이번 달 발행 소설 개수 : 4개
저번 달 발행 소설 개수 : 6개

회차가 존재하는 소설 개수 : 7개
회차가 존재하지 않는 소설 개수(소설2, 소설9, 소설10) : 3개

완결된 소설 개수(소설1, 소설7, 소설8) : 3개
완결되지 않은 소설 개수 : 7개
*/

SET @writer1 := (SELECT user_id FROM user WHERE nickname = 'writer1');
SET @writer2 := (SELECT user_id FROM user WHERE nickname = 'writer2');

INSERT INTO novel (user_id, public_id, title, description, category, is_completed, like_count, total_view_count, period_view_count, published_at, created_at, updated_at, deleted_at)
VALUES
-- ==== novel 1 ====
(
    @writer1,
    'ACx1C085THz1cqtxB3BrV',
    'novel-1',
    'dummy novel',
    'FANTASY',
    TRUE,
    100,
    1000,
    300,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
),
-- ==== novel 2 ====
(
    @writer1,
    'XWVA8ihPlaoStWuEjNDAn',
    'novel-2',
    'dummy novel',
    'FANTASY',
    FALSE,
    0,
    0,
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
),
-- ==== novel 3 ====
(
    @writer1,
    'huX27cVj023JMmh8JInCg',
    'novel-3',
    'dummy novel',
    'ROMANCE',
    FALSE,
    100,
    1005,
    300,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
),
-- ==== novel 4 ====
(
    @writer1,
    '1s7P4zkKkA2wp5WDF6sjY',
    'novel-4',
    'dummy novel',
    'ROMANCE',
    FALSE,
    100,
    1005,
    305,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
-- ==== novel 5 ====
(
    @writer2,
    'qAzo73y2IihPS1d1MU8WX',
    'novel-5',
    'dummy novel',
    'HORROR',
    FALSE,
    115,
    1015,
    315,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    NULL
),
-- ==== novel 6 ====
(
    @writer2,
    'xEjoJEMzNggFskuDABJoA',
    'novel-6',
    'dummy novel',
    'SF',
    FALSE,
    115,
    1015,
    315,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    NULL
),
-- ==== novel 7 ====
(
    @writer2,
    '5KRWtDp74VQs1AdVeGQ-M',
    'novel-7',
    'dummy novel',
    'SF',
    TRUE,
    125,
    1025,
    325,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    NULL
),
-- ==== novel 8 ====
(
    @writer2,
    'T6cRzoAAfp4Ny4_qnDxVX',
    'novel-8',
    'dummy novel',
    'SPORTS',
    TRUE,
    130,
    1030,
    330,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    CURRENT_TIMESTAMP
),
-- ==== novel 9 ====
(
    @writer2,
    'VcAiVnHsUF3YVKNHUFPe9',
    'novel-9',
    'dummy novel',
    'LIFE',
    FALSE,
    0,
    0,
    0,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    CURRENT_TIMESTAMP
),
-- ==== novel 10 ====
(
    @writer2,
    'pC2Kshfduyts53jIVAgHS',
    'novel-10',
    'dummy novel',
    'ETC',
    FALSE,
    0,
    0,
    0,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH),
    NULL
);