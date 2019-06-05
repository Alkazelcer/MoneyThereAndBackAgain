package com.kramarenko.dao;

import com.kramarenko.model.Account;
import com.kramarenko.validation.Result;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Singleton
public class MapAccountDao implements AccountDao {
    private static final String NEGATIVE_AMOUNT_ERROR_MESSAGE = "Amount could not be negative";
    private static final Map<Integer, Account> accounts = new ConcurrentHashMap();
    private static final LongAdder index = new LongAdder();
    private static final Object indexLock = new Object();
    private static final Object updateLock = new Object();

    @Override
    public Optional<Account> getAccountById(int id) {
        Account accFromMap = accounts.get(id);

        if (accFromMap == null) {
            return Optional.empty();
        }

        return Optional.of(Account.copyOf(accFromMap));
    }

    @Override
    public Account createAccount(String name, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(NEGATIVE_AMOUNT_ERROR_MESSAGE);
        }

        int id = generateNextId();
        Account acc = new Account(id, name, amount);
        accounts.put(id, acc);

        return Account.copyOf(acc);
    }

    @Override
    public Optional<Account> updateAccount(int id, Account acc) {
        Account copy = Account.copyOf(acc);

        if (copy.getAmount() < 0) {
            throw new IllegalArgumentException(NEGATIVE_AMOUNT_ERROR_MESSAGE);
        }

        Account accFromMap;

        synchronized (updateLock) {
            accFromMap = accounts.get(id);

            if (accFromMap == null) {
                return Optional.empty();
            }

            accFromMap.setAmount(copy.getAmount());
            accFromMap.setName(copy.getName());
        }

        copy.setId(id);

        return Optional.of(copy);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accounts.values().stream().map(Account::copyOf).collect(Collectors.toList());
    }

    @Override
    public Result<Account> optimisticUpdateAmount(int id, Account acc, double amount) {
        synchronized (updateLock) {
            Account fromMap = accounts.get(id);

            if (Double.compare(fromMap.getAmount(), acc.getAmount()) == 0) {
                fromMap.setAmount(amount);
                return Result.success(Account.copyOf(fromMap));
            }
        }

        return Result.failure();
    }

    private static int generateNextId() {
        synchronized (indexLock) {
            index.increment();
            return index.intValue();
        }
    }
}
