package exception;

public class TransactionNotFoundException extends ExpenseTrackerException{
	public TransactionNotFoundException(String message) {
		super(message);
	}
}
