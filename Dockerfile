# 1. 자바 실행 환경 설정
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 jar 파일을 복사 (gradle 기준)
# 만약 파일 이름이 다르면 build/libs/*.jar 부분을 확인해야 합니다.
COPY build/libs/*.jar app.jar

# 4. 서버 실행
ENTRYPOINT ["java", "-jar", "app.jar"]