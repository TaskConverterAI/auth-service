## Инструкция к запуску auth-service

### Технологии:

- Java 21
- Gradle 7+

### Локальный запуск с in-memory database h2

1. Соберите bootJar: 
```bash
.\gradlew :service:bootJar
```
   - Jar-файл появится в `service/build/libs/service-x.x.x.jar`

2. Запустите `.jar` с активным профилем `dev_h2`: 
```bash
java -jar  service/build/libs/service-1.0.0.jar --spring.profiles.active=dev_h2
```

- Консоль БД доступна по url: http://localhost:8090/h2-console
- API доступно по base path: http://localhost:8090/auth

### Запуск функциональных тестов
 ```bash
 pytest -v --base-url http://localhost:8090 test/auth/test-register.py
 ```