package bank.server.beans;

import bank.server.utility.UniqueIDGenerator;

public class Transaction {
	private int id;
	private String description;
	private String date;
	private String type;
	private double amount;
	private double balance;

	public Transaction(String description, String type, String date, double amount, double balance) {
		this.description = description;
		this.type = type;
		this.date = date;
		this.amount = amount;
		this.balance = balance;
		id = UniqueIDGenerator.getTransID();
	}

	public int getID() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	public double getBalance() {
		return balance;
	}

	@Override
	public String toString() {
		return "Transaction:[TransactionId=" + id + ", Description=" + description + ", Date=" + date + ", Type=" + type
				+ ", Amount=$" + String.format("%.2f", amount) + ", Balance=$" + String.format("%.2f", balance) + "]";
	}
}
