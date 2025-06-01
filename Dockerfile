## 테스트 용도
#FROM eclipse-temurin:17-jdk-alpine
#
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# 1단계: 빌드 스테이지
# 1단계: 빌드 스테이지
FROM --platform=linux/amd64 gradle:8.5.0-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

# 2단계: 실행 스테이지
FROM --platform=linux/amd64 eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]