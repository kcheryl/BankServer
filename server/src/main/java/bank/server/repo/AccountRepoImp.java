package bank.server.repo;

import java.util.ArrayList;
import java.util.List;

import bank.server.beans.Account;

public class AccountRepoImp implements IAccountRepo {
	private ArrayList<Account> list;

	public AccountRepoImp() {
		list = new ArrayList<>();
	}

	public boolean save(Account acc) {
		list.add(acc);
		return true;
	}

	public Account findOneID(int id) {
		if (!list.isEmpty()) {
			for (Account account : list) {
				if (account.getID() == id) {
					return account;
				}
			}
		}
		return null;
	}

	public List<Account> findAll() {
		return list;
	}

}
