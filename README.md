# 사락(Flip)
> 가벼운 도파민 소비형 작품이 아닌, 문학적 깊이와 개성을 지닌 작품을 웹소설처럼 간편하게 읽을 수 있는 플랫폼
<div align="left">
  <a href="https://lucy-blog.notion.site/2990ad45a877803e85b6d597bbce49e4">이슈 로그</a>
  &nbsp;
  <a href="https://lucy-blog.notion.site/3170ad45a877804383a2fe54b3c188d1?v=3170ad45a87780d2aee9000c4040f3f6">개발 문서</a>
</div>

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
docker compose --env-file .env.local up -d
```
### 애플리케이션 실행
```
./gradlew bootRun
```

## 코드 아키텍처
레이어드 아키텍처(Layered Architecture)를 기반으로 최대한 실용적으로 변형한 아키텍처 사용
### 패키지 구조
- ```base```
  - 다른 모듈들이 상속받는 기반 부모 클래스 혹은 인터페이스
- ```common```
  - 프로젝트 전반적으로 쓰이는 공용 모듈(범용 예외, 공통 응답, 유틸리티 등)
- ```config```
  - 애플리케이션의 설정 클래스
- ```core```
  - 애플리케이션 시스템을 구성하는 핵심 모듈(예외 핸들러, 커스텀 검증기, JSON 역직렬화기 등)
- 그 외: 각 도메인별 패키지 (세부 구조는 미정)
### 주요 모듈
- 리팩토링 후 작성 예정
