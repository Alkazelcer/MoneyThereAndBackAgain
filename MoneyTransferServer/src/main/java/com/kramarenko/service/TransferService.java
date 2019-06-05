package com.kramarenko.service;

import com.kramarenko.dao.AccountDao;
import com.kramarenko.model.Account;
import com.kramarenko.validation.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class TransferService {
    private AccountDao dao;

    @Inject
    public TransferService(AccountDao dao) {
        this.dao = dao;
    }

    public Result transfer(int from, int to, double amount) {
        List<String> errors = new ArrayList<>();

        Optional<Account> accFrom = dao.getAccountById(from);
        Optional<Account> accTo = dao.getAccountById(to);

        if (amount < 0) {
            errors.add("Amount cannot be negative");
        }

        if (accFrom.isEmpty()) {
            errors.add("Non existent account with id = " + from);
        }

        if (accTo.isEmpty()) {
            errors.add("Non existent account with id = " + to);
        }

        if (!errors.isEmpty()) {
            return Result.failure(errors);
        }

        if (accFrom.get().getAmount() < amount) {
            errors.add("Insufficient funds on account with id = " + from);
        }

        if (!errors.isEmpty()) {
            return Result.failure(errors);
        }

        Result resultFrom = dao.optimisticUpdateAmount(from, accFrom.get(), accFrom.get().getAmount() -amount);
        if (resultFrom.isSuccess()) {
            accTo.get().setAmount(accTo.get().getAmount() + amount);
            dao.updateAccount(to, accTo.get());
        }

        return Result.success();
    }
}
