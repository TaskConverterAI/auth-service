## CI (Continuous Integration)

1. Скачивает код (actions/checkout);
2. Устанавливает JDK 21;
3. Собирает проект с Gradle (gradlew build);
4. Запускает юнит-тесты;
5. Создаёт bootJar;
6. Сохраняет результат (например, JAR-файл) как артефакт, чтобы можно было скачать с GitHub.

## CD (Continuous Deployment)

1. Собирается Docker-образ (из Dockerfile);
2. Заливается в Docker Hub или GitHub Container Registry;

[TODO] деплоится на staging или production сервер.