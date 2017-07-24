package bank.server.exception;

public class ExceedWithdrawLimitException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExceedWithdrawLimitException(String message) {
		super(message);
	}
}
