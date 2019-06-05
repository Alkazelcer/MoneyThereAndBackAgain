package com.kramarenko.dao;

import com.kramarenko.model.Account;
import com.kramarenko.validation.Result;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    Optional<Account> getAccountById(int id);
    Account createAccount(String name, double amount);
    Optional<Account> updateAccount(int id, Account acc);
    List<Account> getAllAccounts();
    Result<Account> optimisticUpdateAmount(int id, Account acc, double amount);
}
