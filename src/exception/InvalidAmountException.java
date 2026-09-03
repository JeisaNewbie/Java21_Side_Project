package exception;

public class InvalidAmountException extends ExpenseTrackerException {
	public InvalidAmountException(String message) {
		super(message);
	}
}
