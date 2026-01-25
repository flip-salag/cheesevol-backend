# 사락(Flip)
> 가벼운 도파민 소비형 작품이 아닌, 문학적 깊이와 개성을 지닌 작품을 웹소설처럼 간편하게 읽을 수 있는 플랫폼
## [이슈 로그](https://lucy-blog.notion.site/2990ad45a877803e85b6d597bbce49e4)
## [개발 문서](https://lucy-blog.notion.site/Flip-2f20ad45a877802a8a57c382cea7191b)
## ERD
<img width="1830" height="642" alt="novel-service (6)" src="https://github.com/user-attachments/assets/f4d42dbf-efe9-4372-a386-80a7f9e5cac9" />

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

## 코드 아키텍처
레이어드 아키텍처(Layered Architecture)를 기반으로 최대한 실용적으로 변형한 아키텍처 사용
### 패키지 구조
- ```base```
  - 다른 모듈들이 상속받는 공통 부모 클래스(DateAuditEntity, BusinessException 등) 및 루트 응답 DTO(ApiResponse)
- ```common```
  - 프로젝트 전반적으로 쓰이는 공통 모듈(유틸리티, VO 등) 및 공통 예외(DataNotFound 등)
- ```config```
  - 애플리케이션 시스템의 설정 클래스(JpaConfig, WebConfig 등)
- ```core```
  - 애플리케이션 시스템을 구성하는 핵심 모듈(Converter, Validator, Deserializer 등)
- 나머지: 각 도메인별 패키지
### 주요 모듈
- ```Controller```: 각 도메인의 API 표현 및 라우팅
- ```Service```: 각 도메인의 Command(Create, Update, Delete)를 처리
- ```QueryService```: 각 도메인의 Query(Read)를 처리
- ```Command & Query```
  - Service 레이어에서 사용되는 DTO
  - Controller와의 강한 의존성을 끊고 서비스 레이어 메서드 인자 최소화
  - trimming 같은 컨트롤러, 서비스 레이어 어디에 두기에도 애매한 간단하지만 중요한 값 normalization 처리
- ```PolicyValidator```
  - 각 도메인의 정책 검증(중복 제목 검증, 프롤로그 존재 여부 검증 등), Service 모듈들은 직접 정책 검증을 구현하지 않고 적절한 PolicyValidator를 주입받아 사용
  - 도메인 의존성 최소화 및 Service 모듈에 검증 로직이 직접 들어가 책임이 흐려지고 가독성, 확장성, 재사용성, 유지보수성이 떨어지는 문제 방지
- ```Repository```: Spring Data JPA 리포지토리, 간단한 조회 및 exists, bulk update, save 처리
- ```QueryRepository```: QueryDSL 전용 커스텀 리포지토리, 동적 쿼리나 복잡한 쿼리 처리
