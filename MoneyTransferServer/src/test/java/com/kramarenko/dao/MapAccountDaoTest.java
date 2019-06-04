package com.kramarenko.dao;

import com.kramarenko.model.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class MapAccountDaoTest {
    private AccountDao dao;

    @Before
    public void setUp() {
        dao = new MapAccountDao();
    }


    @Test
    public void shouldInsertAccount() {
        //given
        String name = "John Lemon";
        double amount = 100;

        //when
        Account acc = dao.createAccount(name, amount);

        //then
        assertThat(acc.getId()).isNotNull();
        assertThat(acc.getName()).isEqualTo(name);
        assertThat(acc.getAmount()).isCloseTo(amount, within(0.001));
    }

    @Test
    public void shouldInsertTwoAccount() {
        //given
        String name1 = "John Lemon";
        double amount1 = 100;
        String name2 = "John Lemon";
        double amount2 = 50;

        //when
        Account acc1 = dao.createAccount(name1, amount1);
        Account acc2 = dao.createAccount(name2, amount2);

        //then
        assertThat(acc1.getId()).isNotNull();
        assertThat(acc1.getName()).isEqualTo(name1);
        assertThat(acc1.getAmount()).isCloseTo(amount1, within(0.01));

        assertThat(acc2.getId()).isNotNull();
        assertThat(acc2.getName()).isEqualTo(name2);
        assertThat(acc2.getAmount()).isCloseTo(amount2, within(0.01));
    }

    @Test
    public void shouldFailCreateNegativeAmount() {
        //given
        String name = "John Lemon";
        double amount = -100;

        //when
        Throwable thrown = catchThrowable(() -> dao.createAccount(name, amount));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .withFailMessage("Amount could not be negative");
    }

    @Test
    public void shouldReturnInsertedAccount() {
        //given
        String name = "John Lemon";
        double amount = 100;
        Account acc = dao.createAccount(name, amount);

        //when
        Optional<Account> fromStorage = dao.getAccountById(acc.getId());

        //then
        assertThat(fromStorage).isPresent();
        assertThat(fromStorage.get().getId()).isEqualTo(acc.getId());
        assertThat(fromStorage.get().getName()).isEqualTo(name);
        assertThat(fromStorage.get().getAmount()).isCloseTo(amount, within(0.01));
    }

    @Test
    public void shouldReturnEmptyOptionalForNonExistElement() {
        //given
        int nonExistId = 10;
        //when //then
        assertThat(dao.getAccountById(nonExistId)).isNotPresent();
    }

    @Test
    public void shouldUpdateAccount() {
        //given
        String name = "John Lemon";
        double amount = 100;

        Account acc = dao.createAccount(name, amount);
        String newName = "John Doe";
        acc.setName(newName);

        //when
        dao.updateAccount(acc.getId(), acc);

        //then
        assertThat(acc.getId()).isNotNull();
        assertThat(acc.getName()).isEqualTo(newName);
        assertThat(acc.getAmount()).isCloseTo(amount, within(0.001));
    }

    @Test
    public void shouldFailUpdateNegativeAmount() {
        //given
        String name = "John Lemon";
        double amount = 100;

        Account acc = dao.createAccount(name, amount);
        acc.setAmount(-100);

        //when
        Throwable thrown = catchThrowable(() -> dao.updateAccount(acc.getId(), acc));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .withFailMessage("Amount could not be negative");
    }
}
