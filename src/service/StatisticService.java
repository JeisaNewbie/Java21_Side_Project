package service;

import domain.*;
import repository.TransactionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticService {
	
	public StatisticService() {}
	
	public Map<Category, Long> totalByCategory(List<Transaction> transactions) {
		Map<Category, Long> map = new HashMap<>();
		for (IncomeCategory incomeCategory : IncomeCategory.values()) {
			long sum = transactions.stream()
					.filter(t -> t.category() == incomeCategory)
					.mapToLong(Transaction::amount)
					.sum();
			
			map.put(incomeCategory, sum);
		}
		
		for (ExpenseCategory expenseCategory : ExpenseCategory.values()) {
			long sum = transactions.stream()
					.filter(t -> t.category() == expenseCategory)
					.mapToLong(Transaction::amount)
					.sum();
		
			map.put(expenseCategory, sum);
		}
		
		return map;
	}
	
	public long totalIncome(List<Transaction> transactions) {
		long result = 0;
		
		for (Transaction transaction : transactions) {
			if (transaction.type() == TransactionType.INCOME) {
				result += transaction.amount();
			}
		}
		
		return result;
	}
	
	public long totalExpense(List<Transaction> transactions) {
		long result = 0;
		
		for (Transaction transaction : transactions) {
			if (transaction.type() == TransactionType.EXPENSE) {
				result += transaction.amount();
			}
		}
		
		return result;
	}
	
	public long balance(List<Transaction> transactions) {
		long totalIncome = this.totalIncome(transactions);
		long totalExpense = this.totalExpense(transactions);
		
		return totalIncome - totalExpense;
	}
}
