package com.kramarenko.service;

import com.kramarenko.dao.AccountDao;
import com.kramarenko.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

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
    public void shouldReturnAccounts() {
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

    @Test
    public void shouldReturnAccount() {
        //given
        Account account = new Account(1, "John Lemon", 100);
        when(dao.getAccountById(1)).thenReturn(Optional.of(account));

        //when
        Optional<Account> serviceAccount = service.getAccountById(account.getId());

        //then
        assertThat(serviceAccount).hasValue(account);
    }

    @Test
    public void shouldNotReturnAccount() {
        //given
        when(dao.getAccountById(1)).thenReturn(Optional.empty());

        //when
        Optional<Account> serviceAccount = service.getAccountById(1);

        //then
        assertThat(serviceAccount).isEmpty();
    }
}
