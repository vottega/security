# Security

공통 보안(Spring Boot Starter) 모듈입니다.
MVC(서블릿)와 WebFlux(리액티브) 모두에서 동작하며, 게이트웨이가 전달한 헤더를 읽어 SecurityContext에 인증 정보를 채워 넣습니다.

게이트웨이가 인증에 성공하면 아래 헤더를 각 마이크로서비스로 전달합니다.
- X-Client-Role : USER 또는 PARTICIPANT
- X-User-Id
- X-Participant-Id
- X-Room-Id

이 스타터는 해당 헤더를 읽어 사전 인증(pre-auth) 토큰을 생성하고, 보호해야 하는 경로에 대해 인증을 강제합니다.

### 🧰 기술 스택
- Language: Kotlin
- Framework: Spring Boot / Spring Security (MVC + WebFlux 동시 지원)

### ✨ 무엇을 해주나요
Vottega 서비스에서 사용하는 Security Filter들을 공통으로 정의한 라이브러리 입니다.
헤더를 읽어 AuthenticationToken에 사용자 정보를 담아서 줍니다.

### 🛠️ 확장/커스터마이징
- 경로 규칙: vottega.security.filterPaths로 인증 대상 경로를 제어
- 헤더명 변경: vottega.security.headerNames.* 수정
- 비활성화: vottega.security.enabled=false

### 🧪 배포
./gradlew publishMavenJavaPublicationToGithubRepository

### 예시(application.yml)
```
vottega:
  security:
    enabled: true
    filterPaths:
      - /api/**
    headerNames:
      role: X-Client-Role
      userId: X-User-Id
      participantId: X-Participant-Id
      roomId: X-Room-Id
```
