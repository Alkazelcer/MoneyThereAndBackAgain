package com.kramarenko.model;

import java.util.Objects;

public class Account {
    private int id;
    private String name;
    private double amount;

    public Account(int id, String name, double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;
        return Double.compare(account.amount, amount) == 0 &&
                name.equals(account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount);
    }

    public static Account copyOf(Account acc) {
        return new Account(acc.getId(), acc.getName(), acc.getAmount());
    }
}
