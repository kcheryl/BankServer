package bank.server.exception;

public class InsufficientTransactionsException extends Exception {
	private static final long serialVersionUID = 1L;

	public InsufficientTransactionsException(String message) {
		super(message);
	}
}
