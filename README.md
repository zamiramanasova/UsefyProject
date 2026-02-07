[![Build and Test](https://github.com/zamiramanasova/UsefyProject/actions/workflows/ci.yml/badge.svg)](https://github.com/zamiramanasova/UsefyProject/actions/workflows/ci.yml)

 Usefy — Веб-приложение для учёта заявок и курсов

**Usefy** — это веб-приложение для управления образовательными заявками и курсами, реализованное на Spring Boot. Проект демонстрирует навыки backend- и frontend-разработки в рамках единого MVC-приложения.

## ✨ Функциональность
*   **Управление заявками (Applications):** Просмотр, создание, редактирование и удаление заявок.
*   **Управление курсами (Courses):** Аналогичный CRUD для курсов.
*   **Поиск и фильтрация:** Поиск заявок по имени с пагинацией результатов.
*   **Визуальный интерфейс:** Интуитивно понятный веб-интерфейс, построенный на шаблонизаторе Thymeleaf.
*   **Встроенная база данных:** Используется H2 Database для хранения данных.

## 🛠 Стек технологий
*   **Backend:** Java 17, Spring Boot 3
*   **База данных:** H2 Database, Spring Data JPA, Hibernate
*   **Веб-слой:** Spring MVC, Thymeleaf, HTML/CSS (Bootstrap)
*   **Инструменты:** Maven, Lombok, Git
*   **Тестирование:** JUnit 5, Mockito
*   **CI/CD:** GitHub Actions (настройка пайплайна деплоя)

## 🏗 Архитектура
Проект следует классической трёхслойной архитектуре:
1.  **Controller (`/controller`)** — обработка HTTP-запросов и рендеринг представлений.
2.  **Service (`/service`)** — бизнес-логика приложения.
3.  **Repository (`/repository`)** — слой доступа к данным через Spring Data JPA.
4.  **Model (`/model`)** — JPA-сущности (отображаются на таблицы БД).

### Build With
<br>
<p>
<a href="https://img.shields.io">
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white" />
  </a>
</p>
<p>
<a href="https://img.shields.io">
    <img src="https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white" />
  </a>
</p>
<p>
  <a href="https://img.shields.io">
    <img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white" />
  </a>
</p>
<p>
  <a href="https://img.shields.io">
    <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" />
  </a>
</p>
<p>
  <a href="https://img.shields.io">
    <img src="https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white" />
  </a>
</p>
<p>
<a href="https://img.shields.io">
    <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white" />
  </a>
</p>
<p>
<a href="https://img.shields.io">
    <img src="https://img.shields.io/badge/Lombok-000000.svg?style=for-the-badge&logo=lombok&logoColor=white" />
  </a>
</p>

