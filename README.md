# CoinToZ 💸

- [CoinToZ 바로가기](http://ec2-43-201-23-107.ap-northeast-2.compute.amazonaws.com/)
- Z세대들을 위한 커뮤니티 기반 가상화폐 관리 서비스

## 1. 프로젝트 소개 🗒

![image](https://user-images.githubusercontent.com/68420044/218801922-a782127b-37b1-4ec7-a581-99fd61454a0f.png)

***매매일지를 통해 나만의 투자 경험을 쌓아보세요*** 

***나만의 투자경험을 커뮤니티에 공유해 주세요 !***

### 프로젝트 배경 및 목적

- 코로나19 팬데믹 속에서도 전세계적으로 주식시장, 가상화폐 시장은 호황을 이룸
- ‘2030 밀레니얼’ 세대 중심의 ‘입문 투자자’들 증가
- 주식, 암호화폐, 금, 부동산 예적금 등 많은 투자 방식에 대한 경험의 장이 요구됨

### 프로젝트 용도

- ‘2030 밀레니얼’ 세대 중심의 ‘입문 투자자’들 증가함에 따라 투자에 대한 정보 부족 및 무계획 투자 습관을 가지고 있는 Z세대의 입문 투자자들을 위한 서비스입니다.
- 가상화폐 주문과 동시에 거래내역을 통해 수익률이 계산되어 실시간으로 매매일지에 거래기록과 수익률이 저장되며, 또한 일지를 작성하는 습관에 익숙해 질 수 있도록 거래 별 메모 기능을 제공합니다. 또한 투자에 대한 정보를 공유할 수 있도록 커뮤니티 기능을 제공하고 있습니다.
- 이러한 기능을 통해 투자에 대한 정보를 충분히 숙지하지 않은 상황에서 손실을 가져올 수 있는 무지성 투자를 예방하고 자신의 투자패턴, 습관을 파악할 수 있습니다.

### 주요 기능

- 매매일지 : 수익률 계산, 거래내역 저장, 메모(투자계기) 기록 기능
- 거래하기 : 매도/매수 기능, 지정가, 시장가, 예약가 주문 기능
- 커뮤니티 : 프로필 변경 및 게시글, 댓글, 좋아요, 조회 기능
- 코인정보 : 실시간 가상화폐 체결, 호가, 시장가 자료 제공, 가상화폐 캔들 그래프 제공

## 2. 프로젝트 사용방법 📈

- 프로젝트를 설치, 사용하기 위해 필요한 전제조건이 있는가
- 어떻게 설치, 사용, 테스트하는가
- 설치 가이드 문서는 어디에 있는가

## 3. 제작기간 && 팀원소개 🏃‍🏃‍♀️💨

### 2023-01-16 ~ 2023-02-15🔥

| 이름 | 역할 | 담당 기능 |
| --- | --- | --- |
| 강동연 | Project Manager & Developer | 각종 종목 정보(차트,현재가,기사크롤링,랭킹,종목리스트) 불러오기 및 갱신 스케쥴링 / 모의투자 매매 / 게시글 CRUD / 랭킹 / 깃액션배포 / 구글 로그인 |
| 오형상 |  Chief Technical Officer & Developer | JWT를 활용한 인증 / Redis를 활용한 JWT 관리 / OAuth2 적용 / AWS S3적용하여 이미지 저장 / 마이페이지 기능(회원정보, 업비트 키 관리 등) / 수익률 갱신 |
| 김상호 | Plan Maker & Developer | 좋아요 CRUD / 댓글, 커뮤니티UI / 사용자 접근 제한 기능 / 이메일 인증 |
| 강수빈 | Infra & Developer | 업비트 API 주문, 입금, 계좌정보 조회 연동 / 매매일지 CRUD / react,springBoot EC2 배포 |
| 손세열 |  Developer | 메인페이지 UI |
| 임학준 | Developer |  테스트코드 / 대댓글 / 커뮤니티 UI |

## 4. 백엔드 기술 스택 🛠

- Language: **`java`**
- Framework:  **`SPRINGBOOT`**
- Build Tool: **`Gradle`**
- DB: **`MySQL`**,**`Redis`**
- Server: **`AWS EC2 S3`**
- Other Tools : **`GitLab`, `swagger`, `AWS`, `OAuth`, `notion`**

## 5. 아키텍처 📃

![image](https://user-images.githubusercontent.com/68420044/218799236-5060477e-bd9b-4caa-8ccf-3ebd80193d03.png)


## 6. API 명세서 📡

- Swagger: [http://ec2-52-78-23-203.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html](http://ec2-52-78-23-203.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html)
- Notion: [API 명세서 바로가기](https://www.notion.so/API-Docs-7a6e8c5f1ca245459829f1bbff9bb7f2)

## 7. ERD 🗄️

![image](https://user-images.githubusercontent.com/68420044/218799430-27006c22-4ad4-4476-8f3f-929ed20c7697.png)

## 8. 외부 리소스 정보 📁

[업비트 개발자 센터](https://docs.upbit.com/reference/%ED%98%B8%EA%B0%80-%EC%A0%95%EB%B3%B4-%EC%A1%B0%ED%9A%8C)



## 9. Error Code ⚡
| 에러 코드 | 설명 | Http Status |
| --- | --- | --- |
| INVALID_PASSWORD | 비밀번호가 잘못된 경우 | 401 |
| INVALID_TOKEN | 해당 토큰이 만료된 경우 | 401 |
| INVALID_PERMISSION | 사용자가 권한이 없는 경우 | 401 |
| INVALID_AUTH | 잘못된 인증번호를 입력한 경우 | 401 |
| USERNAME_NOT_FOUND | 해당 유저가 없을 경우 | 404 |
| POST_NOT_FOUND | 해당 게시글을 찾을 수 없는 경우 | 404 |
| COMMENT_NOT_FOUND | 해당 댓글을 찾을 수 없는 경우 | 404 |
| LIKE_NOT_FOUND | 좋아요를 누른 적이 없는 경우 | 404 |
| DISLIKE_NOT_FOUND | 싫어요를 누른 적이 없는 경우 | 404 |
| COIN_NOT_FOUND | 해당 코인을 찾을 수 없는 경우 | 404 |
| DIARY_NOT_FOUND | 해당 매매일지를 찾을 수 없는 경우 | 404 |
| MISMATCH_PASSWORD | 비밀번호 불일치한 경우 | 409 |
| DUPLICATED_EMAIL | 이메일 중복되는 경우 | 409 |
| DUPLICATED_LIKE_COUNT | 해당 글에 좋아요를 중복으로 누른 경우 | 409 |
| DUPLICATED_DISLIKE_COUNT | 해당 글에 싫어요를 중복으로 누른 경우 | 409 |
| DATABASE_ERROR | 데이터베이스에 문제가 있는 경우 | 500 |



## 10. Trouble Shooting 🚧

[이메일 인증 기능 구현](https://www.notion.so/05c7fd8b78c941739755363c31d9ddb6)

[업비트에 Post 요청 보내기](https://www.notion.so/Post-1ebe3141e9664befab8c69de840e33d3)

[Axios interceptor를 사용한 토큰 관리](https://www.notion.so/Axios-interceptor-cfce26a076814ce2ab575c383cd9bb20)

[로그인 필수 페이지 접근 제한](https://www.notion.so/2cb687998c304082b18e04b54a367477)
