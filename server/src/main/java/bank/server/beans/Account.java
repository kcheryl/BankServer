package bank.server.beans;

import java.util.ArrayList;
import java.util.List;

public class Account {
	private Customer customer;
	private int id;
	private double balance;
	private List<Transaction> list;

	public Account(int id) {
		list = new ArrayList<>();
		this.id = id;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public int getID() {
		return this.id;
	}

	public double getBalance() {
		return this.balance;
	}

	public List<Transaction> getTransactionList() {
		return this.list;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void addTransaction(Transaction t) {
		list.add(t);
	}

	@Override
	public String toString() {
		return "Account:[Name=" + customer.getName() + ", Id=" + id + ", Balance=$" + String.format("%.2f", balance)
				+ "]";
	}

}
