package bank.server.utility;

public class UniqueIDGenerator {
	private static int accCounter;
	private static int transCounter;
	static {
		accCounter = 0;
		transCounter = 0;
	}

	private UniqueIDGenerator() {

	}

	public static int getAccID() {
		return ++accCounter;
	}

	public static int getTransID() {
		return ++transCounter;
	}
}
