# Описание проекта

REST API сервис с использованием Spring Boot, который будет принимать данные от условного "сенсора". Сенсор будет
отправлять HTTP-запросы с измерениями, содержащими информацию о температуре и погодных условиях.

# Инструкция по запуску

```bash
git clone https://github.com/ExToozy/iot-application
cd iot-application
docker compose up
```

# Примеры взаимодействия с endpoint-ами:

POST http://localhost:8080/api/v1/auth/login

- Request body:

```json
{
  "username": "username",
  "password": "password"
}
```

- Response body (status 200):

```json
{
  "access": "access_token_string",
  "refresh": "refresh_token_string"
}
```

POST http://localhost:8080/api/v1/auth/register

- Request body:

```json
{
  "username": "username",
  "password": "password"
}
```

- Response body (status 200):

```json
{
  "id": 1,
  "username": "username",
  "password": "password"
}
```

POST http://localhost:8080/api/v1/auth/refresh

- Request body:

```json
{
  "refreshToken": "refresh_token_string"
}
```

- Response body (status 200):

```json
{
  "access": "new_access_token_string",
  "refresh": "refresh_token_string"
}
```

POST http://localhost:8080/api/v1/sensors/register

- Request body:

```json
{
  "name": "sensor_name2"
}
```

- Response body (status 200):

```json
{
  "id": 1,
  "name": "sensor_name2"
}
```

POST http://localhost:8080/api/v1/measurements/add

- Request body:

```json
{
  "value": 23.5,
  "raining": true,
  "sensor": {
    "name": "sensor1"
  }
}
```

- Response body (status 200):

```json
{
  "id": 1,
  "value": 23.5,
  "raining": true,
  "sensor": {
    "id": 1,
    "name": "sensor1"
  }
}
```

GET http://localhost:8080/api/v1/measurements

- Response body (status 200):

```json
[
  {
    "id": 1,
    "value": 23.5,
    "raining": true,
    "sensor": {
      "id": 1,
      "name": "sensor1"
    }
  },
  {
    "id": 2,
    "value": 12.2,
    "raining": false,
    "sensor": {
      "id": 1,
      "name": "sensor1"
    }
  }
]
```

GET http://localhost:8080/api/v1/measurements/rainyDaysCount

- Response body (status 200):

```json
5
```