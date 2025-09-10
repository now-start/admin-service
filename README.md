# admin-service

[![Build and Deploy](https://github.com/now-start/admin-service/actions/workflows/build.yaml/badge.svg)](https://github.com/now-start/admin-service/actions/workflows/build.yaml)

**Spring Boot Admin** 서비스로, now-start 마이크로서비스 생태계의 모니터링과 관리를 담당합니다.

🌐 **서비스 URL**: https://spring.nowstart.org

## 📋 주요 기능

- **서비스 모니터링**: 등록된 마이크로서비스들의 상태 실시간 모니터링
- **헬스 체크**: 각 서비스의 헬스 상태 확인
- **로그 관리**: 서비스별 로그 조회 및 관리
- **메트릭 수집**: 애플리케이션 성능 메트릭 수집 및 시각화

## 🛠 기술 스택

- **Framework**: Spring Boot 3.x
- **Admin & Monitoring**
    - `spring-boot-starter-actuator`: 애플리케이션 헬스 체크
    - `de.codecentric:spring-boot-admin-starter-server`: Spring Boot Admin 서버
- **Service Discovery**
    - `spring-cloud-starter-netflix-eureka-client`: Eureka 클라이언트
    - `spring-cloud-starter-config`: 중앙 설정 관리

## 🔧 환경 변수

| 변수명 | 필수 | 설명 |
|--------|------|------|
| `USER_NAME` | ✅ | 관리자 사용자명 |
| `USER_PASSWORD` | ✅ | 관리자 비밀번호 |

## 🐳 Docker 배포

```yaml
services:
  admin-service:
    user: root
    restart: always
    image: ghcr.io/now-start/admin-service:latest
    ports:
      - 8761:8761
    volumes:
      - ./log:/workspace/log
    environment:
      - TZ=Asia/Seoul
      - USER_NAME=admin
      - USER_PASSWORD=your_password
```

## 🚀 CI/CD

이 프로젝트는 [now-start/workflow](https://github.com/now-start/workflow)의 공통 CI/CD 파이프라인을 사용합니다:

- ✅ 자동 버전 관리 및 릴리스
- ✅ Docker 이미지 빌드 및 GHCR 배포  
- ✅ PR 빌드 검증

## 🔗 관련 서비스

- [eureka-service](https://github.com/now-start/eureka-service): 서비스 디스커버리
- [gateway-service](https://github.com/now-start/gateway-service): API 게이트웨이
- [config-service](https://github.com/now-start/config-service): 설정 관리
