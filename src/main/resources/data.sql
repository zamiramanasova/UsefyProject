-- ===== COURSES =====
INSERT INTO courses (title, description)
SELECT 'Java Basics', 'Основы Java'
    WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Java Basics');

INSERT INTO courses (title, description)
SELECT 'Spring Boot', 'Разработка приложений'
    WHERE NOT EXISTS (SELECT 1 FROM courses WHERE title = 'Spring Boot');

-- ===== SECTIONS FOR JAVA BASICS =====
-- Сначала убедимся, что курс существует
DO $$
DECLARE
java_course_id INTEGER;
BEGIN
SELECT id INTO java_course_id FROM courses WHERE title = 'Java Basics';

IF java_course_id IS NOT NULL THEN
        INSERT INTO sections (content, order_index, course_id)
SELECT 'Что такое JVM, JDK и JRE', 1, java_course_id
    WHERE NOT EXISTS (SELECT 1 FROM sections WHERE course_id = java_course_id AND order_index = 1);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Типы данных в Java', 2, java_course_id
    WHERE NOT EXISTS (SELECT 1 FROM sections WHERE course_id = java_course_id AND order_index = 2);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Условия и циклы', 3, java_course_id
    WHERE NOT EXISTS (SELECT 1 FROM sections WHERE course_id = java_course_id AND order_index = 3);
END IF;
END $$;

-- ===== SECTIONS FOR SPRING BOOT =====
DO $$
DECLARE
spring_course_id INTEGER;
BEGIN
SELECT id INTO spring_course_id FROM courses WHERE title = 'Spring Boot';

IF spring_course_id IS NOT NULL THEN
        INSERT INTO sections (content, order_index, course_id)
SELECT 'Что такое Spring Boot', 1, spring_course_id
    WHERE NOT EXISTS (SELECT 1 FROM sections WHERE course_id = spring_course_id AND order_index = 1);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Controllers и REST', 2, spring_course_id
    WHERE NOT EXISTS (SELECT 1 FROM sections WHERE course_id = spring_course_id AND order_index = 2);

INSERT INTO sections (content, order_index, course_id)
SELECT 'Spring Data JPA', 3, spring_course_id
    WHERE NOT EXISTS (SELECT 1 FROM sections WHERE course_id = spring_course_id AND order_index = 3);
END IF;
END $$;