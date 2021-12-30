> 목차
> 
> [**dev-meeting-study**](#dev-meeting-study)
> 
> [1. 토이 프로젝트 목적](#1-토이-프로젝트-목적)
> 
> [1.1 목적 기간 및 달성률](#11-목적-기간-및-달성률)
> 
> [1.2 프로젝트 설명](#12-프로젝트-설명)
> 
> [2. 사용 기술 및 개발 환경](#2-사용-기술-및-개발-환경)<br>
> [Back-end](#back-end)<br>
> [Front-end](#front-end)<br>
> 
> [3. 프로젝트 시작(직접 실행해보셔도 됩니다.!)](#3-프로젝트-시작직접-실행해보셔도-됩니다)
> 
> [4. ERD](#4-erd)
> 
> [5. UI](#5-ui)
> 
> [6. 주요 기능](#6-주요-기능)
> 
> [프로젝트를 진행하며 고민한 Technical Issue](#프로젝트를-진행하며-고민한-technical-issue)
> 
> [프로젝트 기획 구성 <br>](#프로젝트-기획-구성-br)

# dev-meeting-study
- 개발 관련 스터디 리더와 팀원을 이어주는 프로젝트
- 스터디를 무료, 유료와 오프라인, 온라인으로 나눠 팀원을 모집할 수 있게 설계하였습니다.

## 1. 토이 프로젝트 목적
- 클린코드, 코드리뷰 등을 협업 문화를 통해서 점진적으로 개선해나가며 웹 프로젝트를 배포 및 릴리즈 해나가기 위함.
- 하지만 모든 리더, 팀원들의 중도 하차에 의해서 프로젝트가 **중단**되었습니다.

### 1.1 목적 기간 및 달성률
- 본래 프로젝트 기간은 2021-07-27 ~ 한 달이었으나, ~ 2021-12-29까지 진행하게 되었고, 리드미 작성 완료로 마감하게 되었습니다. 
- 프로젝트의 리더 및 팀원이 중도 하차하게 되어 예상했던 것(코드리뷰, 클린코드, 객체 지향 설계)들을 진행하지 못했습니다.
- 프로젝트가 중도에 중단 되면서 로컬에서 도커, 도커 컴포즈만 있으면 동작할 수 있도록 리팩토링 하였습니다.

### 1.2 프로젝트 설명
1. 프로젝트는 도커 컨테이너 기반으로 실행할 수 있도록 하였습니다.
2. docker-compose로 `nginx`(proxy), `node.js`, `jdk`, `mysql` 컨테이너로 구성되어 있습니다.
3. 경량 리눅스(alpine)을 이용하여 개발 서버를 구축하였습니다.

## 2. 사용 기술 및 개발 환경
### Back-end
- Language: `Java 8`
- Web Framework: `Spring Boot v2.5.3`, `Spring security`
- Test Module: `mockito`
- ORM: `JPA` <- `Hibernate`, `querydsl v1.0.10`
- DBMS: `mysql`
- Development Environment: `docker`, `docker-compose`
- Api Document: `swagger2`, `swagger-ui v2.9.2`
- Build tool: `Gradle`

### Front-end
- `Typescript`, `redux`, `React`, `css`, `scss`

## 3. 프로젝트 시작(직접 실행해보셔도 됩니다.!) <span style="color:red;">주의: **gmail 인증 1단계 사용 필요** </span>
- 준비물 : `python3`, `git`, `docker`, `docker-compose`, **Gmail 필요!** : 이메일 인증 보내기 위함.
1. `/etc/hosts` 파일에 도메인 네임 추가 필요!
   - mac os, linux
     - `$ sudo -i`
     - `# echo -e "127.0.0.1 api.dev-meeting-study.site\n127.0.0.1 local.dev-meeting-study.site" >> /etc/hosts`
     - `# exit` -> root 계정에서 빠져나감.
   - window wsl2
     - `C:\Windows\System32\drivers\etc\hosts` 파일을 관리자 권한으로 수정해야합니다.
     - 관리자 권한으로 메모장을 열어 해당 파일을 열고,
     ```
     127.0.0.1 api.dev-meeting-study.site
     127.0.0.1 local.dev-meeting-study.site
     ```
     - 두 줄을 추가해줍시다!
2. `$ cd ~/` : 홈 디렉토리로 이동
3. `git clone https://github.com/xonic789/meeting-study` : 프로젝트 클론
4. `$ cd ~/meeting-study`
5. `$ python init.py` : 파이썬 프로그램 실행
   - 파이썬 프로그램이 하는 일
      1. 구글 이메일, 패스워드 입력 -> ./src/main/resources/email.properties 에 Email,Password 입력
      2. `gradlew clean build` -> 스프링부트로 구성된 자바 어플리케이션 빌드
      3. `docker-compose up -d` -> 백그라운드로 도커 컴포즈 실행
6. 브라우저 창을 열고 local.dev-meeting-study.site 입력해 요청하면 완료!
7. 테스트 유저 
    - email: test@test.com
    - password: 123456


### 4. ERD
[ERD 이미지](readme_image/데브미팅%20프로젝트%20ERD.png)

### 5. UI
[User Interface](https://github.com/xonic789/meeting-study/wiki/User-Interface)

### 6. 주요 기능
- 각 도메인들의 영속화를 위해서 Java 표준 ORM 을 사용하였습니다.
- [각 도메인별 비지니스 로직](https://github.com/xonic789/meeting-study/wiki/Business-Rule)
- [각 도메인별 유즈케이스](https://github.com/xonic789/meeting-study/wiki/Use-Case)

> 사용자
> 1. 회원가입 / 탈퇴
> 2. 로그인 / 로그아웃
> 3. 회원정보 수정
> 4. 스터디 게시글 작성 / 수정 / 삭제 / 조회
> 5. 스터디 유료, 무료 타입 선택
> 6. 스터디 온라인, 오프라인 선택 
> 7. 쪽지 읽기 쓰기


### 프로젝트를 진행하며 고민한 Technical Issue

- [에러처리 통일화](https://devseok.tistory.com/10)
- [개발 서버 통일]()(포스팅 예정, 도커)


### 프로젝트 기획 구성 <br>
[https://devseok.tistory.com/10](https://devseok.tistory.com/10)
