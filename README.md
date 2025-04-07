# PTS API 서버

## 프로젝트 소개

### 포켓 더 스타 (Pocket The Star) 🌟

**프로젝트 비전:**  
"당신의 스타를 주머니에 담다"

**개요:**  
포켓 더 스타는 K-POP 팬들을 위한 팬 커머스 플랫폼입니다. 팬들이 아이돌 굿즈를 효율적으로 수집·관리하고, 중고 거래를 통해 굿즈의 순환 가치를 높이며, 커뮤니티를 통해 소통할 수 있도록 지원합니다.

**핵심 가치 및 주요 기능:**  
- **실시간 시세 정보:** 굿즈의 시장 가격 제공  
- **맞춤 추천:** 관심 아이돌 기반 상품 추천  
- **안전한 거래:** 신뢰성 있는 중고 거래 시스템  
- **팬 커뮤니티:** 팬들 간의 정보 공유와 소통 지원

이렇게 포켓 더 스타는 K-POP 팬들이 굿즈 관리와 거래를 보다 쉽게 할 수 있도록 도와줍니다.

[👉 더 자세한 PTS 개발 이야기 WIKI](https://github.com/pocket-the-star/pts-api-server/wiki)

## 기술 스택

### 백엔드
- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Security** - 인증 및 권한 관리
- **Spring Data JPA** - 데이터 접근 계층
- **QueryDSL** - 타입 세이프한 쿼리 작성
- **JWT** - 토큰 기반 인증
- **Redis** - 캐싱 및 세션 관리
- **Kafka** - 이벤트 기반 비동기 처리
- **MySQL 8.0** - 관계형 데이터베이스
- **Swagger UI** - API 문서화
- **Spring Actuator** - 애플리케이션 모니터링

### 인프라
- **Docker & Docker Compose** - 컨테이너화 및 서비스 오케스트레이션
- **Kafka** - 메시지 브로커링
- **Zookeeper** - 분산 시스템 조정
- **Redis** - 인메모리 데이터 저장소
- **MySQL** - 영구 데이터 저장소

## 아키텍처

이 프로젝트는 헥사고날 아키텍처(포트 및 어댑터 패턴)를 기반으로 구현되었습니다:

- **Domain Layer**: 핵심 비즈니스 로직과 엔티티
  - 도메인 모델: User, Product, Order, Feed 등
  - 도메인 서비스: 비즈니스 규칙 및 로직
- **Application Layer**: 유스케이스 구현 및 서비스 로직
  - 애플리케이션 서비스: UserApplicationService, ProductApplicationService 등
  - DTO: 요청 및 응답 데이터 전송 객체
  - 포트: 인바운드/아웃바운드 인터페이스
- **Infrastructure Layer**: 외부 시스템과의 통합
  - 리포지토리 구현체: JPA, Redis 등
  - 외부 서비스 통합: 이메일, 결제 등
- **Presentation Layer**: API 엔드포인트 및 컨트롤러
  - REST API 컨트롤러
  - 요청/응답 처리

### 주요 패턴 및 기능

- **아웃박스 패턴**: 트랜잭션 일관성을 보장하기 위한 이벤트 발행 패턴
- **분산 락**: Redisson을 활용한 분산 환경에서의 동시성 제어
- **이벤트 기반 아키텍처**: Kafka를 활용한 비동기 이벤트 처리

## 주요 기능

- **사용자 관리**: 회원가입, 로그인, 이메일 인증
  - 이메일 인증 코드 발송 및 확인
  - JWT 기반 인증 및 권한 관리
  - 사용자 프로필 관리
- **상품 관리**: 상품 등록, 조회, 수정, 삭제
  - 상품 카테고리 분류
  - 상품 검색 및 필터링
- **주문 처리**: 주문 생성, 결제 처리, 주문 상태 관리
  - 주문 상태 추적
  - 결제 정보 관리
- **피드 관리**: 사용자 피드 생성 및 관리
  - 피드 작성 및 수정
  - 피드 조회 및 필터링
- **좋아요 기능**: 상품 및 피드에 대한 좋아요 기능
  - 좋아요 추가 및 취소
  - 좋아요 수 집계
- **카테고리 관리**: 상품 및 콘텐츠 분류
  - 계층적 카테고리 구조
  - 카테고리별 상품 조회
- **아이돌 정보 관리**: 아이돌 프로필 및 정보 관리
  - 아이돌 프로필 등록 및 수정
  - 아이돌 관련 상품 및 콘텐츠 관리

## 개발 환경 설정

### 필수 요구사항
- Java 17
- Docker & Docker Compose
- Gradle

### 로컬 개발 환경 설정
1. 저장소 클론
```bash
git clone https://github.com/pocket-the-star/pts-api-server.git
```

2. Docker 컨테이너 실행
```bash
docker-compose up -d
```

## API 문서

API 문서는 Swagger UI를 통해 제공됩니다. 애플리케이션 실행 후 다음 URL에서 접근할 수 있습니다:
```
http://localhost:8080/swagger-ui.html
```

## 테스트

### 테스트 실행
```bash
docker-compose up api_test
```

## 프로젝트 구조

```
src/main/java/com/pts/api/
├── ApiApplication.java
├── user/                  # 사용자 관련 모듈
│   ├── domain/            # 도메인 모델 및 로직
│   │   └── model/         # User, LocalAccount, UserInfo 등
│   ├── application/       # 애플리케이션 서비스
│   │   ├── service/       # UserApplicationService 등
│   │   ├── dto/           # 요청 및 응답 DTO
│   │   └── port/          # 포트 인터페이스
│   ├── infrastructure/    # 인프라 구현체
│   │   └── persistence/   # JPA 리포지토리 구현
│   ├── presentation/      # API 컨트롤러
│   └── common/            # 공통 컴포넌트
├── product/               # 상품 관련 모듈
├── order/                 # 주문 관련 모듈
├── feed/                  # 피드 관련 모듈
├── like/                  # 좋아요 관련 모듈
├── category/              # 카테고리 관련 모듈
├── idol/                  # 아이돌 관련 모듈
├── mail/                  # 이메일 관련 모듈
├── lib/                   # 공통 라이브러리
└── global/                # 전역 설정 및 유틸리티
    ├── common/            # 공통 컴포넌트
    ├── infrastructure/    # 인프라 관련 설정
    ├── lock/              # 분산 락 구현
    ├── outbox/            # 아웃박스 패턴 구현
    └── presentation/      # 전역 프레젠테이션 계층
```

## 회고
아이돌 굿즈를 판매하는 스마트 스토어에 일하면서 팬들이 소통하고 좋아하는 가수의 굿즈를 구매와 판매가 가능한 플랫폼을 만들어 보고싶어 만들게 되었습니다.
카프카를 이용한 이벤트 소싱이나 아웃박스 패턴을 적용하는 부분들을 적용하면서 부가기능이나 특정 비즈니스에 어울리지 않는 로직이 같은 트랜잭션에 들어가 있는걸
분리하면서 흥미롭게 작업한 것 같습니다. 

현재는 유효성검사나 이런 불가결한 로직들로 인해 어플리케이션 계층에 Facade의 형태로 다른 도메인을 의존하고 있지만
추후 평소에 해보고싶던 MSA를 적용하며 프로세스간 통신으로 바꿔 도메인에 대한 분리를 해보는것을 목표로 MVP를 개발하였습니다.
캐싱전략의 경우 Write-Through 를 이용하였는데 MSA로 변환하면서 CQRS를 적용시켜 이벤트 소싱을 통해 좀 더 효율적인 캐싱과 Read로직을 구현해 보려고 합니다.
