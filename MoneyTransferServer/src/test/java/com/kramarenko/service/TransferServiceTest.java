package com.kramarenko.service;

import com.kramarenko.dao.AccountDao;
import com.kramarenko.model.Account;
import com.kramarenko.validation.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {
    private TransferService transferService;

    @Mock
    private AccountDao dao;

    @Before
    public void setUp() {
        transferService = new TransferService(dao);

        String name1 = "John Lemon";
        double amount1 = 100;
        Account account1 = new Account(1, name1, amount1);
        when(dao.getAccountById(1)).thenReturn(Optional.of(account1));

        String name2 = "John Doe";
        double amount2 = 50;
        Account account2 = new Account(2, name2, amount2);
        when(dao.getAccountById(2)).thenReturn(Optional.of(account2));

        when(dao.getAccountById(3)).thenReturn(Optional.empty());
    }

    @Test
    public void shouldReturnFailureAccountFromNotExists() {
        //given

        //when
        Result result = transferService.transfer(3,1, 50);

        //then
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrors()).contains("Non existent account with id = 3");
    }

    @Test
    public void shouldReturnFailureAccountToNotExists() {
        //given

        //when
        Result result = transferService.transfer(1,3, 50);

        //then
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrors()).contains("Non existent account with id = 3");
    }

    @Test
    public void shouldReturnFailureAmountIsNegative() {
        //given

        //when
        Result result = transferService.transfer(2,1, -50);

        //then
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrors()).contains("Amount cannot be negative");
    }

    @Test
    public void shouldReturnFailureInsufficientFunds() {
        //given

        //when
        Result result = transferService.transfer(1,2, 150);

        //then
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrors()).contains("Insufficient funds on account with id = " + 1);
    }

    @Test
    public void shouldReturnSuccess() {
        //given
        when(dao.optimisticUpdateAmount(anyInt(), any(), anyDouble())).thenReturn(Result.success());

        //when
        Result result = transferService.transfer(1,2, 50);

        //then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrors()).isEmpty();
    }
}
