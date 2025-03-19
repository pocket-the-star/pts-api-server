-- 메인 카테고리
INSERT INTO categories (name, created_at, updated_at) VALUES
    ('앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('굿즈', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('포토카드', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 앨범 서브카테고리 (category_id = 1)
INSERT INTO sub_categories (category_id, name, created_at, updated_at) VALUES
    (1, '미니앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 1
    (1, '정규앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 2
    (1, '싱글앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- subCategoryId 3

-- 굿즈 서브카테고리 (category_id = 2)
INSERT INTO sub_categories (category_id, name, created_at, updated_at) VALUES
    (2, '응원봉', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 4
    (2, '슬로건', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 5
    (2, '인형', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 6
    (2, '옷', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 7
    (2, '액세서리', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 8
    (2, '기타', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- subCategoryId 9

-- 포토카드 서브카테고리 (category_id = 3)
INSERT INTO sub_categories (category_id, name, created_at, updated_at) VALUES
    (3, '앨범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 10
    (3, '럭키드로우', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 11
    (3, '팬사인회', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 12
    (3, '시즌그리팅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 13
    (3, '팬미팅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 14
    (3, '콘서트', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 15
    (3, 'MD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 16
    (3, '콜라보', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 17
    (3, '팬클럽', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- subCategoryId 18
    (3, '기타', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- subCategoryId 19