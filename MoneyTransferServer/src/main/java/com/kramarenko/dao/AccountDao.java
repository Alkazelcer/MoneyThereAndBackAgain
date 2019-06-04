package com.kramarenko.dao;

import com.kramarenko.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    Optional<Account> getAccountById(int id);
    Account createAccount(String name, double amount);
    Optional<Account> updateAccount(int id, Account acc);
    List<Account> getAllAccounts();
}
