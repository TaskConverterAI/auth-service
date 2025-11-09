# 1. Используем официальный образ OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# 2. Создаём рабочую папку в контейнере
WORKDIR /app

# 3. Копируем JAR из Gradle сборки
COPY service/build/libs/*.jar app.jar

# 4. Указываем порт приложения
EXPOSE 8081

# 5. Команда для запуска приложения с профилем dev_h2
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=dev_h2"]
