package domain;

import exception.InvalidAmountException;
import exception.InvalidCategoryException;

import java.time.LocalDate;

public record Transaction(int id, LocalDate date, TransactionType type, Category category, long amount, String memo) {
	public Transaction {
		if (amount <= 0) {
			throw new InvalidAmountException("금액은 0 보다 커야 합니다: " + amount);
		}

		if (type == TransactionType.INCOME && !(category instanceof IncomeCategory)) {
			throw new InvalidCategoryException("거래 타입에 해당하는 카테고리가 아닙니다.");
		}

		if (type == TransactionType.EXPENSE && !(category instanceof ExpenseCategory)) {
			throw new InvalidCategoryException("거래 타입에 해당하는 카테고리가 아닙니다.");
		}
	}
}
