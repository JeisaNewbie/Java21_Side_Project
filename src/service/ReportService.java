package service;

import domain.Category;

import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
	public String generateMonthlyReport (YearMonth yearMonth, long income, long expense, long balance, Map<Category, Long> categories) {
		String categoryLines = categories.entrySet().stream()
				.filter(e -> e.getValue() > 0)
				.map(e -> " - %s: %,d원".formatted(e.getKey().label(), e.getValue()))
				.collect(Collectors.joining("\n"));
		
		String result = """
		========================================
		%s 월간 가계부 리포트
		========================================
		총 수입: %,d원
		총 지출: %,d원
		잔액:    %,d원
		----------------------------------------
		카테고리별 내역
		%s
		========================================
		""".formatted(yearMonth, income, expense, balance, categoryLines);
		
		
		return result;
	}
}
