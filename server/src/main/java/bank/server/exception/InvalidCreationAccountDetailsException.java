package bank.server.exception;

public class InvalidCreationAccountDetailsException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidCreationAccountDetailsException(String message) {
		super(message);
	}

}
