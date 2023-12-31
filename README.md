# Система управления задачами
## Описание проекта

Реализация REST API для системы управления задачами.

## Инструкция по локальному запуску проекта

🐳 Перед запуском приложения необходимо "поднять" docker-контейнеры для работы с БД Redis и PostgreSQL.
Для этого выполните команды из окна терминала:

```bash
cd docker   
docker compose up
```

Непосредственно запуск приложения осуществляется с помощью кнопки "Run" на панели IntelliJ Idea, где в конфигурации запуска должен быть
указан класс "SocialNetworkApplication".


## Стек используемых технологий
В данном проекте используются следующие технологии:
* Spring Framework (Boot, Security, OAuth 2.0)
* Hibernate
* JPA
* PostgreSQL
* Lombok
* Docker
* Slf4j