package com.kramarenko.service;

import com.kramarenko.dao.AccountDao;
import com.kramarenko.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountDao dao;

    private AccountService service;

    @Before
    public void setUp() {
        service = new AccountService(dao);
    }

    @Test
    public void shouldReturnAccount() {
        //given
        List<Account> accounts = List.of(new Account(1, "John Lemon", 100),
                new Account(2, "Jaohn Doe", 50));
        when(dao.getAllAccounts()).thenReturn(accounts);

        //when
        List<Account> serviceAccounts = service.getAccounts();

        //then
        assertThat(serviceAccounts).containsOnly(accounts.toArray(Account[]::new));
    }

    @Test
    public void shouldCreateAccount() {
        //given
        String name = "John Lemon";
        double amount = 100;
        Account account = new Account(1, name, amount);
        when(dao.createAccount(anyString(), anyDouble())).thenReturn(account);

        //when
        Account serviceAccount = service.createAccount(name, amount);

        //then
        assertThat(serviceAccount).isEqualTo(account);
    }
}
