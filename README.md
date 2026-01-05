# novel-service
## [이슈 로그](https://lucy-blog.notion.site/2990ad45a877803e85b6d597bbce49e4)
## ERD
<img width="1820" height="642" alt="novel-service (2)" src="https://github.com/user-attachments/assets/27241310-fdcc-48be-b9db-a4e5401fdedd" />

## 기술 스택
### 개발 환경
- Java 17
- Gradle
- Spring Boot 3.5.4
### 데이터베이스
- MySQL 8.4.7

## 시작하기
### .env.local 생성 및 로컬 환경 세팅
```
./scripts/setup-local.sh
```
### 빌드
```
./gradlew build
```
### DB 실행 (로컬에서 애플리케이션 실행 시 먼저 실행 필요)
```
docker compose -f ./docker/db/docker-compose.yml up -d
```
### 애플리케이션 실행
```
./gradlew bootRun
```
