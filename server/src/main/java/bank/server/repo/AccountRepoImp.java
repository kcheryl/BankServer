package bank.server.repo;

import java.util.ArrayList;
import bank.server.beans.Account;

public class AccountRepoImp implements IAccountRepo {
	private ArrayList<Account> list;

	public AccountRepoImp() {
		list = new ArrayList<>();
	}

	public String save(Account acc) {
		list.add(acc);
		return "Account is successfully created," + " id: " + acc.getID();
	}

	public Account findOneName(String name) {
		if (!list.isEmpty()) {
			for (Account account : list) {
				if (account.getName().equals(name)) {
					return account;
				}
			}
		}
		return null;
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

}
