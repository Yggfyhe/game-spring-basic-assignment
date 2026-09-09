## 필수

- Lv 1. 설정 파일 작성: Docker MySQL 연결
  [`docker-compose.yml`](./docker-compose.yml)과 [`application.properties`](./src/main/resources/application.properties)를 추가해 Docker MySQL 데이터소스를 연결함
- Lv 2. 빈 등록 고치기: 의존성 주입
  [`GameService`](./src/main/java/com/gamebasic/game/service/GameService.java)에 누락된 `@Service` 어노테이션을 추가함
- Lv 3. RESTful 경로 맞추기: 게임 목록 API
  [`GameController`](./src/main/java/com/gamebasic/game/controller/GameController.java)의 목록 조회 매핑을 `/game` → `/games`로 수정함
- Lv 4. @Transactional 버그 고치기
  쓰기 작업이 있는 [`GameService`](./src/main/java/com/gamebasic/game/service/GameService.java)의 `createGame`의 `@Transactional(readOnly = true)`를 `@Transactional`로 수정함
- Lv 5. 요청 검증과 응답 DTO: 게임 생성
  [`RunCardRequest`](./src/main/java/com/gamebasic/runcard/dto/RunCardRequest.java)에 Bean Validation 어노테이션을 추가하고 [`CardResponse`](./src/main/java/com/gamebasic/runcard/dto/CardResponse.java)에 필드를 채움
- Lv 6. 보상 카드 선택과 진행 저장
  주석 처리돼 있던 [`GameController`](./src/main/java/com/gamebasic/game/controller/GameController.java)의 `PUT /games/{gameId}/progress` 핸들러를 활성화함
- Lv 7. 목록·상세 조회: 저장된 여정 이어하기
  [`GameRepository`](./src/main/java/com/gamebasic/game/repository/GameRepository.java)/[`GameService`](./src/main/java/com/gamebasic/game/service/GameService.java)에 목록·상세 조회를 구현하고 `GET /games/{gameId}` 핸들러를 추가함
- Lv 8. 변경 감지로 이름 수정, 자식부터 삭제
  이름 변경은 더티 체킹으로, 삭제는 [`RunCardRepository`](./src/main/java/com/gamebasic/runcard/repository/RunCardRepository.java)로 자식부터 지운 뒤 부모를 지우도록 구현함

## 도전

- Lv 9. 끝난 게임 덮어쓰기 막기: 409
  [`GameService`](./src/main/java/com/gamebasic/game/service/GameService.java)의 `updateProgress`에서 `game.isFinished()`면 덮어쓰기 전에 409를 던지도록 수정함
- Lv 10. 전역 예외 처리: 404·409에 message 붙이기
  [`GlobalExceptionHandler`](./src/main/java/com/gamebasic/common/exception/GlobalExceptionHandler.java)에 `GameNotFoundException`/`GameFinishedException` 핸들러를 추가하고 [`GameService`](./src/main/java/com/gamebasic/game/service/GameService.java)가 이 예외들을 던지도록 교체함
- Lv 11. N+1 없는 카드 수 집계와 저장 시간
  [`BaseEntity`](./src/main/java/com/gamebasic/common/entity/BaseEntity.java)로 `createdAt`/`updatedAt`을 채우고, [`RunCardRepository`](./src/main/java/com/gamebasic/runcard/repository/RunCardRepository.java)의 `countByGames`를 [`DeckCount`](./src/main/java/com/gamebasic/runcard/dto/DeckCount.java) DTO 프로젝션으로 구현해 목록의 카드 수를 N+1 없이 집계함
- Lv 12. 랭킹
  (미구현)
