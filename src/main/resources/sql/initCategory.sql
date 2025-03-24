-- 메인 카테고리
INSERT INTO categories (id, name, created_at, updated_at) VALUES
    (1, '앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, '굿즈', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, '포토카드', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
    updated_at = VALUES(updated_at);

INSERT INTO sub_categories (id, category_id, name, created_at, updated_at) VALUES
    (1, 1, '미니앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, '정규앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 1, '싱글앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
    updated_at = VALUES(updated_at);

INSERT INTO sub_categories (id, category_id, name, created_at, updated_at) VALUES
    (4, 2, '응원봉', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 2, '슬로건', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (6, 2, '인형', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (7, 2, '옷', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (8, 2, '액세서리', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (9, 2, '기타', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
    updated_at = VALUES(updated_at);

INSERT INTO sub_categories (id, category_id, name, created_at, updated_at) VALUES
    (10, 3, '앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (11, 3, '럭키드로우', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (12, 3, '팬사인회', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (13, 3, '시즌그리팅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (14, 3, '팬미팅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (15, 3, '콘서트', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (16, 3, 'MD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (17, 3, '콜라보', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (18, 3, '팬클럽', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (19, 3, '기타', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
    updated_at = VALUES(updated_at);