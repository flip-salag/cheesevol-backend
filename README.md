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

## 패키지 구조 및 주요 규칙
```
com.iucyh.cheesevol
├── global/        # 전역 모듈
    ├── base/            # 다른 모듈들이 상속받는 기반 부모 클래스 혹은 인터페이스
    ├── common/          # 프로젝트 전반적으로 쓰이는 공용 모듈(범용 예외, 공통 응답, 유틸리티 등)
    ├── config/          # 애플리케이션의 설정 클래스
    └── core/            # 애플리케이션 시스템을 구성하는 핵심 모듈(예외 핸들러, 커스텀 검증기, JSON 역직렬화기 등)
├── {domain}/      # 각 도메인(+특정 도메인의 이력 등 최상위 도메인에 완전히 종속되는 하위 도메인)별 모듈, 네이밍은 최상위 도메인의 이름(Member, Channel...)
    ├── domain/          # 도메인 객체를 겸하는 JPA Entity 및 도메인에서 사용되는 enum
    ├── repository/      # Spring Data JPA 리포지토리, Redis 리포지토리, QueryDSL 리포지토리 등 데이터 접근 계층
    ├── application/     # 비즈니스 로직을 수행하는 응용 계층, Service
    ├── presentation/    # API Spec을 정의하는 표현 계층, Controller
        ├── dto/               # API 요청/응답 객체
            ├── request/             # 요청 객체(record)
            ├── response/            # 응답 객체(record)
    ├── exception/       # BusinessException을 상속받는 도메인 비즈니스 예외 클래스
        ├── errorcode/         # 각 도메인별 에러코드 enum
    └── infrastructure/  # http 클라이언트 등 외부 시스템과 통신하는 계층
```
**요청/응답 매핑**
- request -> entity, entity -> response, 하위 response -> 상위 response (페이징 응답 등) 매핑들은 각 request/response DTO의 정적 팩토리 메서드에서 구현한다.

**서비스 계층의 인자값/반환값**
- ```application``` 계층의 서비스들은 Requset DTO를 직접 받고, Response DTO를 직접 반환한다. -> 현재는 HTTP API 컨트롤러 이외의 다른 소비자가 없고, 예정된 도입 계획도 없으므로 효율성, 실용성을 중시한다.
  - 단 매핑은 각 요청/응답 객체에서 책임진다.

**검증**
- 도메인의 비즈니스 규칙 검증 로직은 각 도메인의 서비스 클래스에서 ```private```(외부에서도 사용한다면 ```public```) 메서드로 직접 구현한다. 단 다른 도메인의 검증 로직이 필요한 경우(다른 도메인의 예외 클래스를 던지는 검증인 경우) 해당 도메인의 서비스를 의존하여 해결한다. -> 추후 순환 의존 가능성이 생기거나 한 서비스가 검증 로직을 너무 많이 담고 있는 경우 해당 검증과 관련된 관심사만 취급하는 별도의 서비스로 분리할 수 있다.
- 검증 로직은 도메인과 서비스 계층에 모두 존재할 수 있다. 간단하게 도메인의 필드 값만 보면 되는 경우 도메인에, 리포지토리가 필요한 경우 등 좀 더 복잡한 검증인 경우 서비스 계층에 위치한다. 어떠한 경우라도 예외는 도메인 비즈니스 예외만 던진다.

**공통 응답 DTO 래핑**
- 컨트롤러는 도메인 응답 DTO를 공통 응답으로 래핑하지 않고 도메인 응답 DTO 그대로 반환한다. API 공통 응답으로 래핑하는 작업은 ```ApiResponseWrapper``` (공통 응답 자동 래퍼)가 맡는다.
