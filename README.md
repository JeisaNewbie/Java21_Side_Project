# CLI Expense Tracker (커맨드라인 가계부)

## 개요
콘솔에서 수입/지출 내역을 기록, 조회, 관리하는 프로그램. CSV 파일로 데이터를 영속화한다.

## 필수 구현 기능
- 거래 추가 (날짜, 카테고리, 금액, 메모, 수입/지출 구분)
- 전체 거래 목록 조회
- 카테고리별 / 기간별 필터 조회
- 거래 삭제 및 수정
- 카테고리별 합계, 총 수입/지출, 잔액 계산
- CSV 파일로 저장 및 불러오기 (프로그램 재시작 후에도 데이터 유지)
- 잘못된 입력(숫자가 아닌 금액, 존재하지 않는 카테고리 등)에 대한 커스텀 예외 처리
- 텍스트 블록을 이용한 월간 리포트 출력

## 핵심 문법
- `List` / `Map`
- `커스텀 예외`
- `파일 I/O (`try-with-resources`)`
- `enum`
- `텍스트 블록(`"""`)`

## 구현 순서
기능 간 의존 관계(조회는 추가 이후, 수정/삭제는 조회 이후, 집계는 조회/필터 이후, 리포트는 집계 이후)를 고려한 순서. CSV 저장은 로직이 안정된 뒤에 붙여야 "저장 버그"와 "로직 버그"를 구분하기 쉬워서 뒤쪽에 배치.

- [ ] 1. 도메인 모델: `enum`(수입/지출, 카테고리), `Transaction` 클래스, 커스텀 예외 클래스
- [ ] 2. 메모리 기반 저장소(`List<Transaction>`) + 거래 추가 (입력 검증/예외 포함)
- [ ] 3. 전체 목록 조회
- [ ] 4. 거래 수정/삭제
- [ ] 5. 카테고리별 / 기간별 필터 조회
- [ ] 6. 집계 계산 (카테고리별 합계, 총 수입/지출, 잔액)
- [ ] 7. CSV 파일로 저장 및 불러오기 (`try-with-resources`)
- [ ] 8. 월간 리포트 출력 (텍스트 블록)

## 리팩터링 백로그 (우선순위 낮음, 학습용)
필수 기능(1~8) 다 끝내고 여유 있을 때, 학습 목적으로 진행할 항목.

- [ ] 9. `TransactionRepository`의 `findByDate` / `findByCategory` / `findByType`을 `Predicate<Transaction>` 기반 공통 메서드로 통합
  - **왜 Predicate를 쓰는가**: 세 메서드 모두 "리스트를 순회하면서 조건 하나 체크해서 새 리스트에 담기"라는 동일한 구조를 반복하고 있음(코드 중복). 셋의 차이는 오직 "무엇으로 비교하냐"(조건)뿐이고 "어떻게 순회/수집하냐"(로직)는 완전히 같음. `Predicate<T>`는 "T를 받아 `boolean`을 리턴하는 함수"를 변수처럼 전달할 수 있게 해주는 함수형 인터페이스라, "순회/수집 로직은 재사용하고 조건만 갈아끼우고 싶다"는 지금 상황에 정확히 맞는 도구임.
  - 필터 종류가 늘어날수록(예: 금액 범위, 메모 검색 등) 이 패턴의 이점이 커짐 — 새 필터를 추가할 때마다 반복문을 또 짤 필요 없이 조건(람다)만 추가하면 됨.
  - 참고 형태:
    ```java
    private List<Transaction> findBy(Predicate<Transaction> condition) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (condition.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Transaction> findByDate(LocalDate date) {
        return findBy(t -> t.date().equals(date));
    }

    public List<Transaction> findByCategory(Category category) {
        return findBy(t -> t.category() == category);
    }

    public List<Transaction> findByType(TransactionType type) {
        return findBy(t -> t.type() == type);
    }
    ```
