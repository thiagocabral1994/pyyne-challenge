package com.pyyne.challenge.bank.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bank2.integration.Bank2AccountBalance;
import com.bank2.integration.Bank2AccountSource;
import com.bank2.integration.Bank2AccountTransaction;
import com.bank2.integration.Bank2AccountTransaction.TRANSACTION_TYPES;
import com.pyyne.challenge.bank.constant.BankTransactionTypes;
import com.pyyne.challenge.bank.exception.InvalidBankAccountArgumentsException;
import com.pyyne.challenge.bank.exception.InvalidTransactionTypeException;
import com.pyyne.challenge.bank.model.BankAccountBalance;
import com.pyyne.challenge.bank.model.BankAccountTransaction;

public class Bank2AccountSourceAdapter implements BankAccountSourceAdapter {
  private final Bank2AccountSource bank2AccountSource;

  public Bank2AccountSourceAdapter(Bank2AccountSource bank2AccountSource) {
    this.bank2AccountSource = bank2AccountSource;
  }

  public BankAccountBalance getBalance(Long accountId) {
    if (accountId == null) {
      throw new InvalidBankAccountArgumentsException();
    }

    Bank2AccountBalance accountBalance = this.bank2AccountSource.getBalance(accountId);
    double balance = accountBalance.getBalance();
    String currency = accountBalance.getCurrency();

    return new BankAccountBalance(balance, currency);
  }

  public List<BankAccountTransaction> getTransactions(Long accountId, Date fromDate, Date toDate) {
    if (accountId == null ||  fromDate == null || toDate == null) {
      throw new InvalidBankAccountArgumentsException();
    }

    List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();
    List<Bank2AccountTransaction> bank2Transactions = this.bank2AccountSource.getTransactions(accountId, fromDate, toDate);

    for (Bank2AccountTransaction bank2Transaction : bank2Transactions) {
      BankAccountTransaction bankAccountTransaction = Bank2AccountSourceAdapter.convertTransaction(bank2Transaction);
      bankAccountTransactions.add(bankAccountTransaction);
    }

    return bankAccountTransactions;
  }

  private static BankAccountTransaction convertTransaction(Bank2AccountTransaction bank2Transaction) {
    double amount = bank2Transaction.getAmount();

    TRANSACTION_TYPES type = bank2Transaction.getType();
    BankTransactionTypes bankTransactionType = Bank2AccountSourceAdapter.convertType(type);
    
    String text = bank2Transaction.getText();

    return new BankAccountTransaction(amount, bankTransactionType, text);
  }
  
  private static BankTransactionTypes convertType(TRANSACTION_TYPES type) {
    if  (type == TRANSACTION_TYPES.CREDIT) {
      return BankTransactionTypes.CREDIT;
    }

    if (type == TRANSACTION_TYPES.DEBIT) {
      return BankTransactionTypes.DEBIT;
    }

    throw new InvalidTransactionTypeException();
  }
}
