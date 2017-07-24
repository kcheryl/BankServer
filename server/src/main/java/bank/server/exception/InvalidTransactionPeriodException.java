package bank.server.exception;

public class InvalidTransactionPeriodException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionPeriodException(String message) {
		super(message);
	}
}
