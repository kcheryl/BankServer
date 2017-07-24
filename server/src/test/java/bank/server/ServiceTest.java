package bank.server;

import static org.junit.Assert.assertEquals;

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

	@BeforeClass
	public static void setUpClass()
			throws InvalidAccountNameException, InsufficientBalanceException, DuplicateAccountException {
		serv = new ServiceImp();
		serv.createAccount("Jane", 200); // id=1
		serv.createAccount("Peter", 500); // id=2
		serv.createAccount("Wil", 222); // id=3
		serv.createAccount("Joce", 2000); // id=4
		serv.createAccount("Rachel", 170); // id=5
		serv.createAccount("Peach", 550); // id=6
		serv.createAccount("Kim", 1000); // id=7
		serv.createAccount("Vice", 550); // id=8
	}

	// TestCreateAcc ------------------------------------------
	@Test
	public void testCreateAcc() {
		String result;
		try {
			result = serv.createAccount("Rose", 200);
			String expected = "Account is successfully created, id: 11";
			assertEquals(expected, result);
		} catch (InvalidAccountNameException e) {

		} catch (InsufficientBalanceException e) {

		} catch (DuplicateAccountException e) {

		}

	}

	@Test(expected = bank.server.exception.InvalidAccountNameException.class)
	public void testAccNameNull()
			throws InvalidAccountNameException, InsufficientBalanceException, DuplicateAccountException {
		serv.createAccount(null, 100);
	}

	@Test(expected = bank.server.exception.InsufficientBalanceException.class)
	public void testAccBal20()
			throws InvalidAccountNameException, InsufficientBalanceException, DuplicateAccountException {
		serv.createAccount("Wil", 20);
	}

	@Test(expected = bank.server.exception.DuplicateAccountException.class)
	public void testAccDuplicate()
			throws InvalidAccountNameException, InsufficientBalanceException, DuplicateAccountException {
		serv.createAccount("Tim", 120);
		serv.createAccount("Tim", 250);
	}

	// TestShowBal ------------------------------------------
	@Test
	public void testShowBalAcc() throws InvalidAccountException {
		String result = serv.showBalance(1).toString();
		String expected = "Account:[Name=Jane, Id=1, Balance=$200.00]";
		assertEquals(expected, result);
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testAccInvalid() throws InvalidAccountException {
		serv.showBalance(0);
	}

	// TestDeposit ------------------------------------------
	@Test
	public void testDeposit() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException {
		String result = serv.deposit(2, 100.50, "20/4").toString();
		String expected = "Account:[Name=Peter, Id=2, Balance=$600.50]";
		assertEquals(expected, result);
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidDepositAcc() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException {
		serv.deposit(0, 400, "12/7");
	}

	@Test(expected = bank.server.exception.InsufficientBalanceException.class)
	public void testInsufficientBal() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException {
		serv.deposit(2, 0, "5/3");
	}

	@Test(expected = bank.server.exception.InvalidDateException.class)
	public void testInvalidDepositDate() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException {
		serv.deposit(2, 50, "52/1");
	}

	// TestWithdraw ------------------------------------------
	@Test
	public void testWithdraw() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException, ExceedWithdrawLimitException {
		String result = serv.withdraw(3, 100, "20/7").toString();
		String expected = "Account:[Name=Wil, Id=3, Balance=$122.00]";
		assertEquals(expected, result);
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidWithdrawAcc() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException, ExceedWithdrawLimitException {
		serv.withdraw(0, 500, "10/7");
	}

	@Test(expected = bank.server.exception.InsufficientBalanceException.class)
	public void testInsufficientBalance() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException, ExceedWithdrawLimitException {
		serv.withdraw(3, 700, "20/6");
	}

	@Test(expected = bank.server.exception.InvalidDateException.class)
	public void testInvalidWithdrawDate() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException, ExceedWithdrawLimitException {
		serv.withdraw(3, 50, "20/13");
	}

	@Test(expected = bank.server.exception.ExceedWithdrawLimitException.class)
	public void testWithdrawExceed() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException, ExceedWithdrawLimitException {
		serv.withdraw(4, 500, "10/7");
		serv.withdraw(4, 600, "10/7/2017");
	}

	// TestFundTransfer ------------------------------------------
	@Test
	public void testfundTransfer() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InsufficientAmountException, InvalidDateException {
		String result = serv.fundTransfer(5, 6, 50.50, "25/4").toString();
		String expected = "Account:[Name=Rachel, Id=5, Balance=$119.50]";
		assertEquals(expected, result);
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidSrcAcc() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InsufficientAmountException, InvalidDateException {
		serv.fundTransfer(10, 6, 100, "17/6");
	}

	@Test(expected = bank.server.exception.InvalidAccountException.class)
	public void testInvalidDestAcc() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InsufficientAmountException, InvalidDateException {
		serv.fundTransfer(6, 10, 20, "2/6");
	}

	@Test(expected = bank.server.exception.InsufficientAmountException.class)
	public void testInvalidBal() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InsufficientAmountException, InvalidDateException {
		serv.fundTransfer(6, 1, 650, "2/4");
	}

	@Test(expected = bank.server.exception.InvalidDateException.class)
	public void testInvalidDate() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InsufficientAmountException, InvalidDateException {
		serv.fundTransfer(5, 6, 20, "2/40");
	}

	// TestPrintTransactions10 ------------------------------------------
	@Test
	public void testPrintTrans10() throws InvalidAccountException, InvalidDateException, InsufficientBalanceException,
			ExceedWithdrawLimitException, InsufficientAmountException, InsufficientTransactionsException {
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
		String expected = "[Transaction:[TransactionId=4, Description=Deposit, Date=2/2/2017, Type=CR, Amount=$100.00, Balance=$1100.00], "
				+ "Transaction:[TransactionId=5, Description=Deposit, Date=3/2/2017, Type=CR, Amount=$55.00, Balance=$1155.00], "
				+ "Transaction:[TransactionId=6, Description=Transfer to account id: 8, Date=10/2/2017, Type=DR, Amount=$250.00, Balance=$905.00], "
				+ "Transaction:[TransactionId=8, Description=Withdraw, Date=21/2/2017, Type=DR, Amount=$20.00, Balance=$885.00], "
				+ "Transaction:[TransactionId=9, Description=Deposit, Date=3/3/2017, Type=CR, Amount=$350.00, Balance=$1235.00], "
				+ "Transaction:[TransactionId=10, Description=Withdraw, Date=6/3/2017, Type=DR, Amount=$400.00, Balance=$835.00], "
				+ "Transaction:[TransactionId=11, Description=Transfer to account id: 8, Date=10/3/2017, Type=DR, Amount=$40.00, Balance=$795.00], "
				+ "Transaction:[TransactionId=13, Description=Deposit, Date=14/3/2017, Type=CR, Amount=$60.00, Balance=$855.00], "
				+ "Transaction:[TransactionId=14, Description=Deposit, Date=24/3/2017, Type=CR, Amount=$10.00, Balance=$865.00], "
				+ "Transaction:[TransactionId=15, Description=Withdraw, Date=26/3/2017, Type=DR, Amount=$100.00, Balance=$765.00]]";
		assertEquals(expected, result.toString());
	}

	@Test(expected = bank.server.exception.InsufficientTransactionsException.class)
	public void testPrintTrans10Invalid() throws InvalidAccountException, InvalidDateException,
			InsufficientBalanceException, InsufficientTransactionsException {
		serv.deposit(8, 200, "4/6");
		serv.printTransactions10(8);
	}

}
