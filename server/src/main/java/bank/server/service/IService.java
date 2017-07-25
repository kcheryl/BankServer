package bank.server.service;

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

public interface IService {
	public Account createAccount(Customer customer, double balance) throws InvalidCreationAccountDetailsException;

	public Account showBalance(int id) throws InvalidAccountException;

	public Account withdraw(int id, double amount, String date) throws InvalidAccountException,
			InsufficientBalanceException, InvalidDateException, ExceedWithdrawLimitException;

	public Account deposit(int id, double amount, String date)
			throws InvalidAccountException, InvalidDateException, InsufficientBalanceException;

	public Account fundTransfer(int sourceId, int destId, double amount, String date)
			throws InvalidAccountException, InsufficientAmountException, InvalidDateException;

	public List<Transaction> printTransactions10(int id) throws InsufficientTransactionsException;

	public List<Transaction> printTransactionsPeriod(int id, String startDate, String endDate)
			throws InvalidDateException, InsufficientTransactionsException;
}
