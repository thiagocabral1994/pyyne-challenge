package com.pyyne.challenge.bank.model;

import java.util.Objects;

public class BankAccountBalance {

    private final double balance;
    private final String currency;

    public BankAccountBalance(double balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountBalance that = (BankAccountBalance) o;
        return Double.compare(that.balance, balance) == 0 && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, currency);
    }
}
