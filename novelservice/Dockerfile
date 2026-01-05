FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# gradlew 복사
COPY gradlew ./
COPY gradle ./gradle

# 빌드 및 설정 파일 복사(의존성 캐싱용)
COPY build.gradle settings.gradle /app/
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사 및 빌드
COPY src /app/src
RUN chmod +x ./gradlew && ./gradlew bootJar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]