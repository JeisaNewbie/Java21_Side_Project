package domain;

public enum ExpenseCategory implements Category {
	HOUSING,           // 주거비
	FOOD,              // 식비
	LIVING,            // 생활비
	GROOMING,          // 꾸밈비
	SELF_DEVELOPMENT,  // 자기계발비
	TRANSPORT;         // 교통비
	
	@Override
	public String label() {
		return name();
	}
}
