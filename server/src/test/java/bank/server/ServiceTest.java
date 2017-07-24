package bank.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import bank.server.beans.Transaction;
import bank.server.exception.DuplicateAccountException;
import bank.server.exception.ExceedWithdrawLimitException;
import bank.server.exception.InsufficientAmountException;
import bank.server.exception.InsufficientBalanceException;
import bank.server.exception.InsufficientTransactionsException;
import bank.server.exception.InvalidAccountException;
import bank.server.exception.InvalidAccountNameException;
import bank.server.exception.InvalidDateException;
import bank.server.service.ServiceImp;

public class ServiceTest {
	static ServiceImp serv;
	private static final String UNEXPECTED_EXCEPTION = "Unexpected exception";

	@BeforeClass
	public static void setUpClass() {
		serv = new ServiceImp();
		try {
			serv.createAccount("Jane", 200); // id=1
			serv.createAccount("Peter", 500); // id=2
			serv.createAccount("Wil", 222); // id=3
			serv.createAccount("Joce", 2000); // id=4
			serv.createAccount("Rachel", 170); // id=5
			serv.createAccount("Peach", 550); // id=6
			serv.createAccount("Kim", 1000); // id=7
			serv.createAccount("Vice", 550); // id=8
			serv.createAccount("Wallace", 300); // id=9
			serv.createAccount("Pat", 740); // id=10
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	// TestCreateAcc ------------------------------------------
	@Test
	public void testCreateAcc() {
		String result;
		try {
			result = serv.createAccount("Rose", 200);
			String expected = "Account is successfully created, id: 13";
			assertEquals(expected, result);
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidAccountNameException.class)
	public void testAccNameNull() throws InvalidAccountNameException {
		try {
			serv.createAccount(null, 100);
		} catch (InsufficientBalanceException | DuplicateAccountException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InsufficientBalanceException.class)
	public void testAccBal20() throws InsufficientBalanceException {
		try {
			serv.createAccount("Wil", 20);
		} catch (InvalidAccountNameException | DuplicateAccountException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.DuplicateAccountException.class)
	public void testAccDuplicate() throws DuplicateAccountException {
		try {
			serv.createAccount("Tim", 120);
			serv.createAccount("Tim", 250);
		} catch (InvalidAccountNameException | InsufficientBalanceException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	// TestShowBal ------------------------------------------
	@Test
	public void testShowBalAcc() {
		try {
			String result = serv.showBalance(1).toString();
			String expected = "Account:[Name=Jane, Id=1, Balance=$200.00]";
			assertEquals(expected, result);
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testAccInvalid() throws InvalidAccountException {
		serv.showBalance(0);
	}

	// TestDeposit ------------------------------------------
	@Test
	public void testDeposit() {
		try {
			String result = serv.deposit(2, 100.50, "20/4").toString();
			String expected = "Account:[Name=Peter, Id=2, Balance=$600.50]";
			assertEquals(expected, result);
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidDepositAcc() throws InvalidAccountException {
		try {
			serv.deposit(0, 400, "12/7");
		} catch (InsufficientBalanceException | InvalidDateException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InsufficientBalanceException.class)
	public void testInsufficientBal() throws InsufficientBalanceException {
		try {
			serv.deposit(2, 0, "5/3");
		} catch (InvalidAccountException | InvalidDateException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidDateException.class)
	public void testInvalidDepositDate() throws InvalidDateException {
		try {
			serv.deposit(2, 50, "52/1");
		} catch (InvalidAccountException | InsufficientBalanceException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	// TestWithdraw ------------------------------------------
	@Test
	public void testWithdraw() {
		try {
			String result = serv.withdraw(3, 100, "20/7").toString();
			String expected = "Account:[Name=Wil, Id=3, Balance=$122.00]";
			assertEquals(expected, result);
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidWithdrawAcc() throws InvalidAccountException {
		try {
			serv.withdraw(0, 500, "10/7");
		} catch (InsufficientBalanceException | InvalidDateException | ExceedWithdrawLimitException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InsufficientBalanceException.class)
	public void testInsufficientBalance() throws InsufficientBalanceException {
		try {
			serv.withdraw(3, 700, "20/6");
		} catch (InvalidAccountException | InvalidDateException | ExceedWithdrawLimitException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidDateException.class)
	public void testInvalidWithdrawDate() throws InvalidDateException {
		try {
			serv.withdraw(3, 50, "20/13");
		} catch (InvalidAccountException | InsufficientBalanceException | ExceedWithdrawLimitException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.ExceedWithdrawLimitException.class)
	public void testWithdrawExceed() throws ExceedWithdrawLimitException {
		try {
			serv.withdraw(4, 500, "10/7");
			serv.withdraw(4, 600, "10/7/2017");
		} catch (InvalidAccountException | InsufficientBalanceException | InvalidDateException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	// TestFundTransfer ------------------------------------------
	@Test
	public void testfundTransfer() {
		try {
			String result = serv.fundTransfer(5, 6, 50.50, "25/4").toString();
			String expected = "Account:[Name=Rachel, Id=5, Balance=$119.50]";
			assertEquals(expected, result);
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidSrcAcc() throws InvalidAccountException {
		try {
			serv.fundTransfer(20, 6, 100, "17/6");
		} catch (InsufficientAmountException | InvalidDateException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidDestAcc() throws InvalidAccountException {
		try {
			serv.fundTransfer(6, 20, 20, "2/6");
		} catch (InsufficientAmountException | InvalidDateException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InsufficientAmountException.class)
	public void testInvalidBal() throws InsufficientAmountException {
		try {
			serv.fundTransfer(6, 1, 1000, "2/4");
		} catch (InvalidAccountException | InvalidDateException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidDateException.class)
	public void testInvalidDate() throws InvalidDateException {
		try {
			serv.fundTransfer(5, 6, 20, "2/40");
		} catch (InvalidAccountException | InsufficientAmountException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	// TestPrintTransactions10 ------------------------------------------
	@Test
	public void testPrintTrans10() {
		try {
			serv.deposit(7, 100, "2/2");
			serv.deposit(7, 55, "3/2");
			serv.fundTransfer(7, 8, 250, "10/2");
			serv.withdraw(7, 20, "21/2");
			serv.deposit(7, 350, "3/3");
			serv.withdraw(7, 400, "6/3");
			serv.fundTransfer(7, 8, 40, "10/3");
			serv.deposit(7, 60, "14/3");
			serv.deposit(7, 10, "24/3");
			serv.withdraw(7, 100, "26/3");
			List<Transaction> result = serv.printTransactions10(7);
			String expected = "[Transaction:[TransactionId=9, Description=Deposit, Date=2/2/2017, Type=CR, Amount=$100.00, Balance=$1100.00], "
					+ "Transaction:[TransactionId=10, Description=Deposit, Date=3/2/2017, Type=CR, Amount=$55.00, Balance=$1155.00], "
					+ "Transaction:[TransactionId=11, Description=Transfer to account id: 8, Date=10/2/2017, Type=DR, Amount=$250.00, Balance=$905.00], "
					+ "Transaction:[TransactionId=13, Description=Withdraw, Date=21/2/2017, Type=DR, Amount=$20.00, Balance=$885.00], "
					+ "Transaction:[TransactionId=14, Description=Deposit, Date=3/3/2017, Type=CR, Amount=$350.00, Balance=$1235.00], "
					+ "Transaction:[TransactionId=15, Description=Withdraw, Date=6/3/2017, Type=DR, Amount=$400.00, Balance=$835.00], "
					+ "Transaction:[TransactionId=16, Description=Transfer to account id: 8, Date=10/3/2017, Type=DR, Amount=$40.00, Balance=$795.00], "
					+ "Transaction:[TransactionId=18, Description=Deposit, Date=14/3/2017, Type=CR, Amount=$60.00, Balance=$855.00], "
					+ "Transaction:[TransactionId=19, Description=Deposit, Date=24/3/2017, Type=CR, Amount=$10.00, Balance=$865.00], "
					+ "Transaction:[TransactionId=20, Description=Withdraw, Date=26/3/2017, Type=DR, Amount=$100.00, Balance=$765.00]]";
			assertEquals(expected, result.toString());
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InsufficientTransactionsException.class)
	public void testPrintTrans10Invalid() throws InsufficientTransactionsException {
		try {
			serv.deposit(8, 200, "4/6");
			serv.printTransactions10(8);
		} catch (InsufficientBalanceException | InvalidDateException | InvalidAccountException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test
	public void testPrintTransPeriod() {
		try {
			serv.deposit(9, 20, "2/3");
			serv.deposit(9, 200, "3/3");
			serv.deposit(9, 25, "4/5");
			serv.deposit(9, 10, "10/5");
			List<Transaction> result = serv.printTransactionsPeriod(9, "3/3", "4/5");
			String expected = "[Transaction:[TransactionId=4, Description=Deposit, Date=3/3/2017, Type=CR, Amount=$200.00, Balance=$520.00], "
					+ "Transaction:[TransactionId=5, Description=Deposit, Date=4/5/2017, Type=CR, Amount=$25.00, Balance=$545.00]]";
			assertEquals(expected, result.toString());
		} catch (Exception e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

	@Test(expected = bank.server.exception.InvalidDateException.class)
	public void testPrintTransPeriodInvalid() throws InvalidDateException {
		try {
			serv.deposit(10, 200, "4/1");
			serv.printTransactionsPeriod(10, "2/5", "5/4");
		} catch (InsufficientBalanceException | InvalidAccountException | InsufficientTransactionsException e) {
			fail(UNEXPECTED_EXCEPTION);
		}
	}

}
