# 1단계: 빌드용 이미지 (Gradle 사용)
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행용 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app
# 빌드된 jar 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]