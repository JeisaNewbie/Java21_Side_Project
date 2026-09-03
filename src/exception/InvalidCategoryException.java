package exception;

public class InvalidCategoryException extends ExpenseTrackerException {
	public InvalidCategoryException(String message) {
		super(message);
	}
}
