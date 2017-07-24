package bank.server.service;

import java.util.ArrayList;
import bank.server.beans.Account;
import bank.server.beans.Transaction;
import bank.server.exception.DuplicateAccountException;
import bank.server.exception.ExceedWithdrawLimitException;
import bank.server.exception.InsufficientAmountException;
import bank.server.exception.InsufficientBalanceException;
import bank.server.exception.InvalidAccountException;
import bank.server.exception.InvalidAccountNameException;
import bank.server.exception.InvalidDateException;

public interface IService {
	public String createAccount(String name, double balance)
			throws InvalidAccountNameException, InsufficientBalanceException, DuplicateAccountException;

	public Account showBalance(int id) throws InvalidAccountException;

	public Account withdraw(int id, double amount, String date) throws InvalidAccountException,
			InsufficientBalanceException, InvalidDateException, ExceedWithdrawLimitException;

	public Account deposit(int id, double amount, String date)
			throws InvalidAccountException, InvalidDateException, InsufficientBalanceException;

	public Account fundTransfer(int sourceId, int destId, double amount, String date)
			throws InvalidAccountException, InsufficientAmountException, InvalidDateException;

	public ArrayList<Transaction> printTransactions10(int id);

	public ArrayList<Transaction> printTransactionsPeriod(int id, String startDate, String endDate);
}
