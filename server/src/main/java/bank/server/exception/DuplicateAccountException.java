package bank.server.exception;

public class DuplicateAccountException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateAccountException(String message) {
		super(message);
	}
}
