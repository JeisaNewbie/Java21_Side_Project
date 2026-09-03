package repository;

import domain.Category;
import domain.Transaction;
import domain.TransactionType;
import exception.TransactionNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
	private final List<Transaction> transactions = new ArrayList<>();
	private int nextId = 1;
	
	public Transaction add(LocalDate date, TransactionType type, Category category, long amount, String memo) {
		Transaction transaction = new Transaction(nextId, date, type, category, amount, memo);
		transactions.add(transaction);
		
		nextId++;
		return transaction;
	}
	
	public List<Transaction> findAll() {
		
		/*
			return transactions;
			
			이러면 호출한 쪽에서 repository.findAll().clear()처럼 repository
			내부 상태를 외부에서 직접 조작할 수 있게 돼요. 캡슐화가 깨지는 거죠.
			방어적 복사본을 리턴하는 게 안전합니다.
		 */
		
		return List.copyOf(transactions);
	}
	
	public List<Transaction> findByDate(LocalDate date) {
		List<Transaction> result = new ArrayList<>();
		
		for (Transaction transaction : this.transactions) {
			if (transaction.date().equals(date)) {
				result.add(transaction);
			}
		}
		
		return result;
	}
	
	public List<Transaction> findByCategory(Category category) {
		List<Transaction> result = new ArrayList<>();
		
		for (Transaction transaction : this.transactions) {
			if (transaction.category() == category) {
				result.add(transaction);
			}
		}
		
		return result;
	}
	
	public List<Transaction> findByType(TransactionType type) {
		List<Transaction> result = new ArrayList<>();
		
		for (Transaction transaction : this.transactions) {
			if (transaction.type() == type) {
				result.add(transaction);
			}
		}
		
		return result;
	}
	
	public void update(int id, LocalDate date, TransactionType type, Category category, long amount, String memo) {
		int index = -1;
		
		/*
			id가 있는지 확인하는 예외처리 필요?
			
			->	네, 필요합니다. 존재하지 않는 id로 수정/삭제를 시도하면
				지금은 (버그를 고친 뒤 기준으로도) 아무 일도 안 일어나고 조용히 넘어가는데,
				사용자 입장에선 "삭제됐는지 안 됐는지" 알 방법이 없어요. exception 패키지에
				TransactionNotFoundException extends ExpenseTrackerException 하나 추가해서
				위처럼 던지는 걸 추천합니다.
		 */
		
		for (int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).id() == id) {
				index = i;
			}
		}
		
		if  (index == -1) {
			throw new TransactionNotFoundException("존재하지 않는 id입니다: " + id);
		}
		
		Transaction transaction = new Transaction(id, date, type, category, amount, memo);
		
		transactions.set(index, transaction);
	}
	
	public void delete(int id) {
		boolean removed = transactions.removeIf(transaction -> transaction.id() == id);
		
		if (!removed) {
			throw new TransactionNotFoundException("존재하지 않는 id입니다: " + id);
		}
	}

}
