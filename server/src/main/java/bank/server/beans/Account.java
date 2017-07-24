package bank.server.beans;

import java.util.ArrayList;
import java.util.List;

import bank.server.utility.UniqueIDGenerator;

public class Account {
	private String name;
	private int id;
	private double balance;
	private List<Transaction> list;

	public Account(String name, double balance) {
		this.name = name;
		this.balance = balance;
		list = new ArrayList<>();
		id = UniqueIDGenerator.getAccID();
	}

	public String getName() {
		return this.name;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void addTransaction(Transaction t) {
		list.add(t);
	}

	@Override
	public String toString() {
		return "Account:[Name=" + name + ", Id=" + id + ", Balance=$" + String.format("%.2f", balance) + "]";
	}

}
