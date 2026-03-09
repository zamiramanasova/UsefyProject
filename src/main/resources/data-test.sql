-- Для тестов используем простые INSERT без ALTER SEQUENCE
-- ===== COURSES =====
INSERT INTO courses (title, description) VALUES
                                             ('Java Basics', 'Основы Java'),
                                             ('Spring Boot', 'Разработка приложений');

-- ===== SECTIONS =====
INSERT INTO sections (content, order_index, course_id) VALUES
                                                           ('Что такое JVM, JDK и JRE', 1, 1),
                                                           ('Типы данных в Java', 2, 1),
                                                           ('Условия и циклы', 3, 1),
                                                           ('Что такое Spring Boot', 1, 2),
                                                           ('Controllers и REST', 2, 2),
                                                           ('Spring Data JPA', 3, 2);