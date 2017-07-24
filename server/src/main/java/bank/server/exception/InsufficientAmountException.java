package bank.server.exception;

public class InsufficientAmountException extends Exception {
	private static final long serialVersionUID = 1L;

	public InsufficientAmountException(String message) {
		super(message);
	}

}
