# REST API ExploreWithMe

Платформа для пользователей, которая позволяет информацией об интересных событиях и помогать найти компанию для участия
в них.

Микросервисное приложение: публичный API и сервис статистики.

**Стек технологий:** Spring Boot, Spring Data, Spring Rest Template, PostgreSQL, Docker, Lombok.

**Swagger документация:**
ewm-main-service-spec.json, ewm-stats-service-spec.json.

Инструкция по развертыванию проекта:

1. Clone c github
2. mvn clean package
3. docker-compose up

## Функциональность Комментарии
Пользователи могут оставлять комментарии на опубликованные события независимо от его времени начала или окончания.
Перед опубликованием комментария он должен пройти модерацию администратором.

**Состояния комментария:** PENDING, CONFIRMED, REJECTED

### Public API
Неавторизованные пользователи могут просматривать только одобренные админом комментарии по ID события.

GET /events/{eventId}/comments

GET /events/{eventId}/comments/{commentId}

### Private API
Авторизованный пользователь может создавать комментарии, просматривать свои комментарии независимо от их статуса,
а также комментарии других пользователей по ID события, одобренные админом.
Пользователь может изменять свои комментарии до момента их публикации, удалять свои комментарии.

### Admin API
Администратор может одобрять или отклонять комментарии пользователей.

PATCH /admin/comments/{commentId}/publish

PATCH /admin/comments/{commentId}/reject

GET /admin/comments - с опциональными параметрами

**Pull Request** https://github.com/artemklinkov/java-explore-with-me/pull/2