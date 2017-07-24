package bank.server.beans;

import bank.server.utility.UniqueIDGenerator;

public class Transaction implements Comparable<Transaction> {
	private int id;
	private String description;
	private String date;
	private String type;
	private double amount;
	private double balance;

	public Transaction(String description, String type, String date, double amount) {
		this.description = description;
		this.type = type;
		this.date = date;
		this.amount = amount;
		balance += amount;
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
		return "Transaction : [id=" + id + ", description=" + description + ", date=" + date + ", type=" + type
				+ ", amount=" + String.format("%.2f", balance) + ", balance=" + String.format("%.2f", balance) + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Transaction)) {
			return false;
		} else {
			Transaction tmp = (Transaction) o;
			return (tmp.id == this.id && tmp.description.equals(this.description) && tmp.date.equals(this.date)
					&& tmp.type.equals(this.type) && tmp.amount == this.amount && tmp.balance == this.balance);
		}
	}

	public int compareTo(Transaction o) {
		return o.getID() - this.getID();
	}
}
