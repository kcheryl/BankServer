package bank.server;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import bank.server.exception.DuplicateAccountException;
import bank.server.exception.ExceedWithdrawLimitException;
import bank.server.exception.InsufficientAmountException;
import bank.server.exception.InsufficientBalanceException;
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

	}

	// TestCreateAcc ------------------------------------------
	@Test
	public void testCreateAcc()
			throws InvalidAccountNameException, InsufficientBalanceException, DuplicateAccountException {
		String result = serv.createAccount("Rose", 200);
		String expected = "Account is successfully created, id: 9";
		assertEquals(expected, result);
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
	public void testShowBalAcc() throws InvalidAccountException, InvalidAccountNameException,
			InsufficientBalanceException, DuplicateAccountException {
		String result = serv.showBalance(1).toString();
		String expected = "Account : [name=Jane, id=1, balance=$200.00]";
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
		String expected = "Account : [name=Peter, id=2, balance=$600.50]";
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
		String expected = "Account : [name=Wil, id=3, balance=$122.00]";
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
	public void testWithdrawExceed1000() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InvalidDateException, ExceedWithdrawLimitException {
		serv.withdraw(4, 500, "10/7");
		serv.withdraw(4, 501, "10/7/2017");
	}

	// TestFundTransfer ------------------------------------------
	@Test
	public void testfundTransfer() throws InvalidAccountNameException, InsufficientBalanceException,
			DuplicateAccountException, InvalidAccountException, InsufficientAmountException, InvalidDateException {
		String result = serv.fundTransfer(5, 6, 50.50, "25/4").toString();
		String expected = "Account : [name=Rachel, id=5, balance=$119.50]";
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

}
