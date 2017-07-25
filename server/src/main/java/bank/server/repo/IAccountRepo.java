package bank.server.repo;

import java.util.List;

import bank.server.beans.Account;

public interface IAccountRepo {
	public boolean save(Account acc);

	public Account findOneID(int id);

	public List<Account> findAll();
}
