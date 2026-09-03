package domain;

public enum IncomeCategory implements Category {
	SALARY,             // 급여
	SIDE_INCOME,        // 부수입
	ALLOWANCE,          // 용돈
	INTEREST_DIVIDEND,  // 이자/배당
	BONUS,              // 상여금
	REFUND              // 환급금
	;
	
	@Override
	public String label() {
		return name();
	}
}
