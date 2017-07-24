package bank.server.exception;

public class InvalidAccountNameException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidAccountNameException(String message) {
		super(message);
	}
}
