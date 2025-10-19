## Инструкция к запуску auth-service

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
