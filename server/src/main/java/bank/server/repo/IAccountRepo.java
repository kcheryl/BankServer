package bank.server.repo;

import bank.server.beans.Account;

public interface IAccountRepo {
	public String save(Account acc);

	public Account findOneName(String name);

	public Account findOneID(int id);
}
