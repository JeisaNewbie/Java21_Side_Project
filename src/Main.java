import domain.ExpenseCategory;
import domain.IncomeCategory;
import domain.Transaction;
import domain.TransactionType;
import repository.TransactionRepository;
import service.StatisticService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		runAggregationTest();
	}

	// 집계 서비스 검증용 임시 실행 코드. service 계층 완성 후에는 지우고 실제 CLI 메뉴 루프로 교체할 것.
	private static void runAggregationTest() {
		TransactionRepository repository = new TransactionRepository();
		seedSampleData(repository);

		List<Transaction> transactions = repository.findAll();
		System.out.println("===== 시드 데이터 (" + transactions.size() + "건) =====");
		transactions.forEach(System.out::println);

		System.out.println();
		System.out.println("===== 예상값 - 전체 기간(findAll) 기준 =====");
		System.out.println("FOOD 합계: 35,000 / TRANSPORT 합계: 3,000 / LIVING 합계: 7,000");
		System.out.println("총 지출: 45,000 / 총 수입: 100,000 / 잔액: 55,000");

		System.out.println();
		System.out.println("===== 예상값 - 2026년 9월만(findByMonth) 기준 =====");
		System.out.println("FOOD 합계: 15,000 / TRANSPORT 합계: 3,000 / LIVING 합계: 7,000 (8월 FOOD 20,000은 제외되어야 함)");
		System.out.println("총 지출: 25,000 / 총 수입: 100,000 / 잔액: 75,000");
		
		StatisticService statisticsService = new StatisticService();
		System.out.println(statisticsService.totalByCategory(transactions)); // 전체 기간
		List<Transaction> septemberOnly = repository.findByMonth(YearMonth.of(2026, 9));
		System.out.println(statisticsService.totalByCategory(septemberOnly)); // 9월만

		// totalIncome / totalExpense / balance 시그니처는 totalByCategory와 동일하게
		// List<Transaction>을 받아 long을 리턴한다고 가정함. 다르게 설계하셨다면 아래 호출부만 맞춰 고치면 됨.
		System.out.println();
		System.out.println("===== totalIncome / totalExpense / balance 검증 =====");
		System.out.println("[전체 기간] 예상 - 총수입: 100,000 / 총지출: 45,000 / 잔액: 55,000");
		System.out.println("[전체 기간] 실제 - 총수입: " + statisticsService.totalIncome(transactions)
				+ " / 총지출: " + statisticsService.totalExpense(transactions)
				+ " / 잔액: " + statisticsService.balance(transactions));

		System.out.println("[9월만]   예상 - 총수입: 100,000 / 총지출: 25,000 / 잔액: 75,000");
		System.out.println("[9월만]   실제 - 총수입: " + statisticsService.totalIncome(septemberOnly)
				+ " / 총지출: " + statisticsService.totalExpense(septemberOnly)
				+ " / 잔액: " + statisticsService.balance(septemberOnly));
	}

	// 계산하기 쉬운 라운드 넘버로 구성 — 결과를 눈으로 바로 검증하기 위함
	private static void seedSampleData(TransactionRepository repository) {
		LocalDate day1 = LocalDate.of(2026, 9, 1);
		LocalDate day2 = LocalDate.of(2026, 9, 2);
		LocalDate augustDay = LocalDate.of(2026, 8, 15);

		// FOOD 합계 15,000 (day1: 10,000 + day2: 5,000)
		repository.add(day1, TransactionType.EXPENSE, ExpenseCategory.FOOD, 10_000, "점심");
		repository.add(day2, TransactionType.EXPENSE, ExpenseCategory.FOOD, 5_000, "저녁");

		// TRANSPORT 합계 3,000
		repository.add(day1, TransactionType.EXPENSE, ExpenseCategory.TRANSPORT, 3_000, "버스");

		// LIVING 합계 7,000
		repository.add(day2, TransactionType.EXPENSE, ExpenseCategory.LIVING, 7_000, "생필품");

		// 총 수입 100,000
		repository.add(day1, TransactionType.INCOME, IncomeCategory.SALARY, 100_000, "월급");

		// 8월 거래 — findByMonth(9월) 필터링 시 이 항목은 제외되어야 함
		repository.add(augustDay, TransactionType.EXPENSE, ExpenseCategory.FOOD, 20_000, "8월 회식");

		// 전체 기간: 총 지출 45,000(=15,000+3,000+7,000+20,000), 잔액 55,000
		// 9월만: 총 지출 25,000(=15,000+3,000+7,000), 잔액 75,000
	}
}
