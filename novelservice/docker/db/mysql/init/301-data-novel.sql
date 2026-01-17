USE `novel_service`;

/*
==== 작가 당 소설 개수 ====
writer1 : 4개
writer2 : 6개

==== 소설 분포 ====
삭제되지 않은 소설 개수 : 7개
삭제된 소설 개수 : 3개

이번 달 생성 소설 개수 : 4개
저번 달 생성 소설 개수 : 6개

회차가 존재하는 소설 개수 (모두 공통으로 6개씩 -> 프롤로그 1개, 일반 회차 5개) : 8개
회차가 존재하지 않는 소설 개수 : 2개

완결된 소설 개수 : 4개
완결되지 않은 소설 개수 : 6개
*/

SET @writer1 := (SELECT user_id FROM user WHERE nickname = 'writer1');
SET @writer2 := (SELECT user_id FROM user WHERE nickname = 'writer2');

INSERT INTO novel (user_id, public_id, title, description, category, common_episode_count, is_completed, last_episode_publish_date, like_count, total_view_count, period_view_count, max_episode_number, created_at, updated_at, deleted_at)
SELECT
    @writer1,
    'ACx1C085THz1cqtxB3BrV',
    'novel-1',
    'dummy novel',
    'FANTASY',
    5,
    FALSE,
    NOW(),
    100,
    1000,
    300,
    5,
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    NOW(),
    NULL
UNION ALL
SELECT
    @writer1,
    'XWVA8ihPlaoStWuEjNDAn',
    'novel-2',
    'dummy novel',
    'FANTASY',
    5,
    FALSE,
    NOW(),
    100,
    1000,
    300,
    5,
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    NOW(),
    NULL
UNION ALL
SELECT
    @writer1,
    'huX27cVj023JMmh8JInCg',
    'novel-3',
    'dummy novel',
    'ROMANCE',
    5,
    FALSE,
    DATE_SUB(NOW(), INTERVAL 3 HOUR),
    100,
    1005,
    300,
    5,
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    NOW(),
    NULL
UNION ALL
SELECT
    @writer1,
    '1s7P4zkKkA2wp5WDF6sjY',
    'novel-4',
    'dummy novel',
    'ROMANCE',
    5,
    FALSE,
    DATE_SUB(NOW(), INTERVAL 4 HOUR),
    100,
    1005,
    305,
    5,
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    NOW(),
    NULL
UNION ALL
SELECT
    @writer2,
    'qAzo73y2IihPS1d1MU8WX',
    'novel-5',
    'dummy novel',
    'HORROR',
    5,
    FALSE,
    DATE_SUB(NOW(), INTERVAL 5 DAY),
    115,
    1015,
    315,
    5,
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    NULL
UNION ALL
SELECT
    @writer2,
    'xEjoJEMzNggFskuDABJoA',
    'novel-6',
    'dummy novel',
    'SF',
    5,
    FALSE,
    DATE_SUB(NOW(), INTERVAL 5 DAY),
    115,
    1015,
    315,
    5,
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    NULL
UNION ALL
SELECT
    @writer2,
    '5KRWtDp74VQs1AdVeGQ-M',
    'novel-7',
    'dummy novel',
    'SF',
    5,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 7 DAY),
    125,
    1025,
    325,
    5,
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    NULL
UNION ALL
SELECT
    @writer2,
    'T6cRzoAAfp4Ny4_qnDxVX',
    'novel-8',
    'dummy novel',
    'SPORTS',
    5,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 8 DAY),
    130,
    1030,
    330,
    5,
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    NOW()
UNION ALL
SELECT
    @writer2,
    'VcAiVnHsUF3YVKNHUFPe9',
    'novel-9',
    'dummy novel',
    'LIFE',
    0,
    TRUE,
    NULL,
    0,
    0,
    0,
    0,
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    NOW()
UNION ALL
SELECT
    @writer2,
    'pC2Kshfduyts53jIVAgHS',
    'novel-10',
    'dummy novel',
    'ETC',
    0,
    TRUE,
    NULL,
    0,
    0,
    0,
    0,
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    DATE_SUB(NOW(), INTERVAL 1 MONTH),
    NOW();