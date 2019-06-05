package com.kramarenko.service;

import com.kramarenko.dao.AccountDao;
import com.kramarenko.model.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
public class AccountService {
    private static final Logger LOG = Logger.getLogger(AccountService.class.getName());
    private AccountDao dao;

    @Inject
    public AccountService(AccountDao dao) {
        this.dao = dao;
    }

    public List<Account> getAccounts() {
        LOG.info("Getting accounts from data storage");
        return dao.getAllAccounts();
    }

    public Account createAccount(String name, double amount) {
        LOG.info("Inserting account with name = " + name + " and amount = " + amount +
                "to data storage");
        return dao.createAccount(name, amount);
    }

    public Optional<Account> getAccountById(int id) {
        LOG.info("Getting account with id = " + id + " from data storage");
        return dao.getAccountById(id);
    }
}
