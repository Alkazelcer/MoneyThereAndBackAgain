package com.kramarenko.service;

import com.kramarenko.dao.AccountDao;
import com.kramarenko.model.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AccountService {
    private AccountDao dao;

    @Inject
    public AccountService(AccountDao dao) {
        this.dao = dao;
    }

    public List<Account> getAccounts() {
        return dao.getAllAccounts();
    }

    public Account createAccount(String name, double amount) {
        return dao.createAccount(name, amount);
    }
}
