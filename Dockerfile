# 1단계: 빌드용 이미지
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .

# [중요] 실행 권한을 부여합니다 (이게 없어서 126 에러가 난 거예요)
RUN chmod +x ./gradlew

# 빌드 진행
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행용 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]