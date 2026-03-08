-- ОЧИЩАЕМ ТАБЛИЦЫ (если нужно)
DELETE FROM sections;
DELETE FROM courses;

-- ===== СБРАСЫВАЕМ СЧЁТЧИКИ ID (для PostgreSQL) =====
ALTER SEQUENCE courses_id_seq RESTART WITH 1;
ALTER SEQUENCE sections_id_seq RESTART WITH 1;

-- ===== COURSES =====
INSERT INTO courses (title, description) VALUES
                                             ('Java Basics', 'Основы Java'),
                                             ('Spring Boot', 'Разработка приложений');

-- ===== SECTIONS FOR JAVA BASICS =====
INSERT INTO sections (content, order_index, course_id) VALUES
                                                           ('Что такое JVM, JDK и JRE — это фундаментальные понятия Java. JVM выполняет байт-код, JDK включает инструменты разработки, JRE — среда выполнения.', 1, 1),
                                                           ('В Java есть примитивные типы: int, double, boolean и ссылочные типы (классы, интерфейсы, массивы).', 2, 1),
                                                           ('Условные операторы if-else и циклы for, while позволяют управлять потоком выполнения программы.', 3, 1);

-- ===== SECTIONS FOR SPRING BOOT =====
INSERT INTO sections (content, order_index, course_id) VALUES
                                                           ('Spring Boot — это инструмент для быстрой разработки Spring приложений с минимальной конфигурацией.', 1, 2),
                                                           ('Контроллеры обрабатывают HTTP запросы и возвращают ответы. @RestController и @RequestMapping — основные аннотации.', 2, 2),
                                                           ('Spring Data JPA упрощает работу с базами данных, предоставляя готовые реализации CRUD операций.', 3, 2);