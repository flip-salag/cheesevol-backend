# 치즈볼 (CheeseVol)
> 치지직 채널별 볼륨 자동 저장 / 자동 적용 확장 프로그램

## 기술 스택
### 개발 환경
- Java 17
- Gradle
- Spring Boot 3.5.4
### 데이터베이스 및 접근 기술
- MySQL 8.4
- h2 (test)
- JPA, Spring Data JPA
- QueryDSL 7.1 (openfeign)

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
docker compose --env-file .env.local up -d
```
### 애플리케이션 실행
```
./gradlew bootRun
```

## 패키지별 역할
- ```base```
  - 다른 모듈들이 상속받는 기반 부모 클래스 혹은 인터페이스
- ```common```
  - 프로젝트 전반적으로 쓰이는 공용 모듈(범용 예외, 공통 응답, 유틸리티 등)
- ```config```
  - 애플리케이션의 설정 클래스
- ```core```
  - 애플리케이션 시스템을 구성하는 핵심 모듈(예외 핸들러, 커스텀 검증기, JSON 역직렬화기 등)
- 그 외: 각 도메인별 패키지
### 주요 모듈
- 리팩토링 후 작성 예정
