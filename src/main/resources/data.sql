-- ===== COURSES =====
-- Вставляем курсы только если их нет
INSERT INTO courses (title, description)
SELECT 'Java Basics', 'Основы Java'
    WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Java Basics');

INSERT INTO courses (title, description)
SELECT 'Spring Boot', 'Разработка приложений'
    WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Spring Boot');

-- ===== SECTIONS FOR JAVA BASICS =====
-- Вставляем секции для Java Basics
INSERT INTO sections (content, order_index, course_id)
SELECT 'Что такое JVM, JDK и JRE', 1, id
FROM courses
WHERE title = 'Java Basics'
  AND NOT EXISTS (
    SELECT 1 FROM sections s
    WHERE s.course_id = (SELECT id FROM courses WHERE title = 'Java Basics')
      AND s.order_index = 1
);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Типы данных в Java', 2, id
FROM courses
WHERE title = 'Java Basics'
  AND NOT EXISTS (
    SELECT 1 FROM sections s
    WHERE s.course_id = (SELECT id FROM courses WHERE title = 'Java Basics')
      AND s.order_index = 2
);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Условия и циклы', 3, id
FROM courses
WHERE title = 'Java Basics'
  AND NOT EXISTS (
    SELECT 1 FROM sections s
    WHERE s.course_id = (SELECT id FROM courses WHERE title = 'Java Basics')
      AND s.order_index = 3
);

-- ===== SECTIONS FOR SPRING BOOT =====
INSERT INTO sections (content, order_index, course_id)
SELECT 'Что такое Spring Boot', 1, id
FROM courses
WHERE title = 'Spring Boot'
  AND NOT EXISTS (
    SELECT 1 FROM sections s
    WHERE s.course_id = (SELECT id FROM courses WHERE title = 'Spring Boot')
      AND s.order_index = 1
);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Controllers и REST', 2, id
FROM courses
WHERE title = 'Spring Boot'
  AND NOT EXISTS (
    SELECT 1 FROM sections s
    WHERE s.course_id = (SELECT id FROM courses WHERE title = 'Spring Boot')
      AND s.order_index = 2
);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Spring Data JPA', 3, id
FROM courses
WHERE title = 'Spring Boot'
  AND NOT EXISTS (
    SELECT 1 FROM sections s
    WHERE s.course_id = (SELECT id FROM courses WHERE title = 'Spring Boot')
      AND s.order_index = 3
);