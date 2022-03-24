package com.pyyne.challenge.bank.model;

import com.pyyne.challenge.bank.constant.BankTransactionTypes;

import java.util.Objects;


public class BankAccountTransaction {
    private final double amount;
    private final BankTransactionTypes type;
    private final String text;

    public BankAccountTransaction(double amount, BankTransactionTypes type, String text) {
        this.amount = amount;
        this.type = type;
        this.text = text;
    }

    public double getAmount() {
        return amount;
    }

    public BankTransactionTypes getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountTransaction that = (BankAccountTransaction) o;
        return Double.compare(that.amount, amount) == 0 && type == that.type && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, type, text);
    }
}
