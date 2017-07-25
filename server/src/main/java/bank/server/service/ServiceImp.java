package bank.server.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bank.server.beans.Account;
import bank.server.beans.Customer;
import bank.server.beans.Transaction;
import bank.server.exception.ExceedWithdrawLimitException;
import bank.server.exception.InsufficientAmountException;
import bank.server.exception.InsufficientBalanceException;
import bank.server.exception.InsufficientTransactionsException;
import bank.server.exception.InvalidAccountException;
import bank.server.exception.InvalidCreationAccountDetailsException;
import bank.server.exception.InvalidDateException;
import bank.server.repo.IAccountRepo;
import bank.server.utility.UniqueIDGenerator;

public class ServiceImp implements IService {
	private IAccountRepo repo;

	public ServiceImp(IAccountRepo repo) {
		this.repo = repo;
	}

	public Account createAccount(Customer customer, double balance) throws InvalidCreationAccountDetailsException {
		String empty = "";
		if (customer.getName() == null || customer.getName().equals(empty)) {
			return null;
		}
		if (balance < 100) {
			throw new InvalidCreationAccountDetailsException("Insufficient starting balance");
		}

		int id = UniqueIDGenerator.getAccID();
		Account newAcc = new Account(id);
		newAcc.setCustomer(customer);
		newAcc.setBalance(balance);

		checkDuplicate(customer);
		if (repo.save(newAcc)) {
			return newAcc;
		}
		return null;
	}

	private void checkDuplicate(Customer customer) throws InvalidCreationAccountDetailsException {
		List<Account> existingAccList = repo.findAll();
		if (!existingAccList.isEmpty()) {
			for (Account acc : existingAccList) {
				if (acc.getCustomer().getName().equals(customer.getName())) {
					throw new InvalidCreationAccountDetailsException("An account has already been created");
				}
			}
		}
	}

	public Account showBalance(int id) throws InvalidAccountException {
		Account acc = repo.findOneID(id);
		if (acc == null) {
			throw new InvalidAccountException("Account does not exist");
		}
		return acc;
	}

	public Account withdraw(int id, double amount, String date) throws InvalidAccountException,
			InsufficientBalanceException, InvalidDateException, ExceedWithdrawLimitException {
		Account acc = repo.findOneID(id);
		if (acc == null) {
			throw new InvalidAccountException("Account does not exist for withdrawal");
		}
		if (amount <= 0 || acc.getBalance() < amount) {
			throw new InsufficientBalanceException("Insufficient balance for withdrawal");
		}
		String parsedDate = checkDate(date);
		if (parsedDate == null) {
			throw new InvalidDateException("Invalid date for withdrawal");
		}

		return processWithdraw(acc, amount, parsedDate);
	}

	public Account processWithdraw(Account acc, double amount, String parsedDate) throws ExceedWithdrawLimitException {
		List<Transaction> list = acc.getTransactionList();
		int totalAmt = 0;
		for (Transaction trans : list) {
			String withdraw = "Withdraw";
			if (trans.getDate().equals(parsedDate) && trans.getDescription().equals(withdraw)) {
				totalAmt += trans.getAmount();
			}
		}
		if (totalAmt + amount > 1000) {
			throw new ExceedWithdrawLimitException("Exceeded limit for withdrawal");
		}

		double newBal = acc.getBalance() - amount;
		acc.setBalance(newBal);
		String descrip = "Withdraw";
		String type = "DR";
		Transaction trans = new Transaction(descrip, type, parsedDate, amount, newBal);
		acc.addTransaction(trans);
		return acc;
	}

	public Account deposit(int id, double amount, String date)
			throws InvalidAccountException, InvalidDateException, InsufficientBalanceException {
		Account acc = repo.findOneID(id);
		if (acc == null) {
			throw new InvalidAccountException("Account does not exist for deposit");
		}
		if (amount <= 0) {
			throw new InsufficientBalanceException("Insufficient balance for deposit");
		}
		String parsedDate = checkDate(date);
		if (parsedDate == null) {
			throw new InvalidDateException("Invalid date for deposit");
		}

		double newBal = acc.getBalance() + amount;
		acc.setBalance(newBal);
		String descrip = "Deposit";
		String type = "CR";
		Transaction trans = new Transaction(descrip, type, parsedDate, amount, newBal);
		acc.addTransaction(trans);
		return acc;
	}

	public String checkDate(String date) {
		String[] str = date.split("/");
		int day = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		int year;
		if (str.length == 3) {
			year = Integer.parseInt(str[2]);
		} else {
			year = 2017;
		}

		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		try {
			c.set(year, month, day);
			c.getTime();
		} catch (Exception e) {
			return null;
		}
		return day + "/" + month + "/" + year;
	}

	public Account fundTransfer(int sourceId, int destId, double amount, String date)
			throws InvalidAccountException, InsufficientAmountException, InvalidDateException {
		Account srcAcc = repo.findOneID(sourceId);
		if (srcAcc == null) {
			throw new InvalidAccountException("Source account does not exist");
		}
		Account destAcc = repo.findOneID(destId);
		if (destAcc == null) {
			throw new InvalidAccountException("Destination account does not exist");
		}
		if (amount <= 0 || srcAcc.getBalance() < amount) {
			throw new InsufficientAmountException("Insufficient amount to transfer");
		}
		String parsedDate = checkDate(date);
		if (parsedDate == null) {
			throw new InvalidDateException("Invalid date");
		}

		double newBal = srcAcc.getBalance() - amount;
		srcAcc.setBalance(newBal);
		String descrip = "Transfer to account id: " + destAcc.getID();
		String type = "DR";
		Transaction trans = new Transaction(descrip, type, parsedDate, amount, newBal);
		srcAcc.addTransaction(trans);

		newBal = destAcc.getBalance() + amount;
		destAcc.setBalance(newBal);
		descrip = "Transfer from account id: " + srcAcc.getID();
		type = "CR";
		trans = new Transaction(descrip, type, parsedDate, amount, newBal);
		destAcc.addTransaction(trans);

		return srcAcc;
	}

	public List<Transaction> printTransactions10(int id) throws InsufficientTransactionsException {
		Account acc = repo.findOneID(id);
		List<Transaction> transList = acc.getTransactionList();
		if (transList.size() < 10) {
			throw new InsufficientTransactionsException("Insufficient transactions to print");
		}

		int index = transList.size() - 10;
		List<Transaction> newList = new ArrayList<>();
		for (int i = index; i < transList.size(); i++) {
			newList.add(transList.get(i));
		}
		return newList;
	}

	public List<Transaction> printTransactionsPeriod(int id, String startDate, String endDate)
			throws InvalidDateException, InsufficientTransactionsException {
		String parsedStartDate = checkDate(startDate);
		String parsedEndDate = checkDate(endDate);
		if (parsedStartDate == null || compareDate(parsedStartDate, parsedEndDate) > 0) {
			throw new InvalidDateException("Invalid start date");
		}

		if (parsedEndDate == null) {
			throw new InvalidDateException("Invalid end date");
		}

		Account acc = repo.findOneID(id);
		List<Transaction> transList = acc.getTransactionList();
		if (transList.isEmpty()) {
			throw new InsufficientTransactionsException("Insufficient transactions to print");
		}

		return processTransactionsPeriod(transList, parsedStartDate, parsedEndDate);
	}

	public List<Transaction> processTransactionsPeriod(List<Transaction> transList, String parsedStartDate,
			String parsedEndDate) {
		List<Transaction> newList = new ArrayList<>();
		for (Transaction trans : transList) {
			int startValue = compareDate(parsedStartDate, trans.getDate());
			int endValue = compareDate(parsedEndDate, trans.getDate());
			if (startValue <= 0 && endValue >= 0) {
				newList.add(trans);
			}
		}

		return newList;
	}

	// 0 if equal, <0 if before the argument time, >0 if after the argument time
	public int compareDate(String date1, String date2) {
		return convertDate(date1).compareTo(convertDate(date2));
	}

	public Calendar convertDate(String date) {
		String[] str = date.split("/");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.valueOf(str[2]), Integer.valueOf(str[1]), Integer.valueOf(str[0]));
		return cal;
	}

}
