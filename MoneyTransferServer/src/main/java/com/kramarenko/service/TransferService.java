package com.kramarenko.service;

import com.kramarenko.dao.AccountDao;
import com.kramarenko.model.Account;
import com.kramarenko.validation.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
public class TransferService {
    private static final Logger LOG = Logger.getLogger(TransferService.class.getName());
    private AccountDao dao;

    @Inject
    public TransferService(AccountDao dao) {
        this.dao = dao;
    }

    public Result transfer(int from, int to, double amount) {
        LOG.info("Transfering money = " + amount + "from account = " + from + " to = " + to);

        List<String> errors = new ArrayList<>();

        Optional<Account> accFrom = dao.getAccountById(from);
        Optional<Account> accTo = dao.getAccountById(to);

        if (amount < 0) {
            LOG.info("Amount is negative");
            errors.add("Amount cannot be negative");
        }

        if (accFrom.isEmpty()) {
            LOG.info("Non existent account with id = " + from);
            errors.add("Non existent account with id = " + from);
        }

        if (accTo.isEmpty()) {
            LOG.info("Non existent account with id = " + to);
            errors.add("Non existent account with id = " + to);
        }

        if (!errors.isEmpty()) {
            return Result.failure(errors);
        }

        if (accFrom.get().getAmount() < amount) {
            LOG.info("Insufficient funds on account with id = " + from);
            errors.add("Insufficient funds on account with id = " + from);
        }

        if (!errors.isEmpty()) {
            return Result.failure(errors);
        }

        Result resultFrom = dao.optimisticUpdateAmount(from, accFrom.get(), accFrom.get().getAmount() -amount);
        if (resultFrom.isSuccess()) {
            accTo.get().setAmount(accTo.get().getAmount() + amount);
            dao.updateAccount(to, accTo.get());

            LOG.info("Transfer is successful money = " + amount + "from account = " + from + " to = " + to);
            return Result.success();
        }

        return Result.failure();
    }
}
