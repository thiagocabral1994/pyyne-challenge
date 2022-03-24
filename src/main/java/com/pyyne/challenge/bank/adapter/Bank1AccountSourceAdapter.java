package com.pyyne.challenge.bank.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bank1.integration.Bank1AccountSource;
import com.bank1.integration.Bank1Transaction;
import com.pyyne.challenge.bank.constant.BankTransactionTypes;
import com.pyyne.challenge.bank.exception.InvalidBankAccountArgumentsException;
import com.pyyne.challenge.bank.exception.InvalidTransactionTypeException;
import com.pyyne.challenge.bank.model.BankAccountBalance;
import com.pyyne.challenge.bank.model.BankAccountTransaction;

public class Bank1AccountSourceAdapter implements BankAccountSourceAdapter {
  private final Bank1AccountSource bank1AccountSource;

  public Bank1AccountSourceAdapter(Bank1AccountSource bank1AccountSource) {
    this.bank1AccountSource = bank1AccountSource;
  }

  public BankAccountBalance getBalance(Long accountId) {
    if (accountId == null) {
      throw new InvalidBankAccountArgumentsException();
    }

    double balance = this.bank1AccountSource.getAccountBalance(accountId);
    String currency = this.bank1AccountSource.getAccountCurrency(accountId);

    return new BankAccountBalance(balance, currency);
  }

  public List<BankAccountTransaction> getTransactions(Long accountId, Date fromDate, Date toDate) {
    if (accountId == null || fromDate == null || toDate == null) {
      throw new InvalidBankAccountArgumentsException();
    }

    List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();
    List<Bank1Transaction> bank1Transactions = this.bank1AccountSource.getTransactions(accountId, fromDate, toDate);

    for (Bank1Transaction bank1Transaction : bank1Transactions) {
      BankAccountTransaction bankAccountTransaction = Bank1AccountSourceAdapter.convertTransaction(bank1Transaction);
      bankAccountTransactions.add(bankAccountTransaction);
    }

    return bankAccountTransactions;
  }

  private static BankAccountTransaction convertTransaction(Bank1Transaction bank1Transaction) {
    double amount = bank1Transaction.getAmount();

    int type = bank1Transaction.getType();
    BankTransactionTypes bankTransactionType = Bank1AccountSourceAdapter.convertType(type);
    
    String text = bank1Transaction.getText();

    return new BankAccountTransaction(amount, bankTransactionType, text);
  }
  
  private static BankTransactionTypes convertType(int type) {
    if  (type == Bank1Transaction.TYPE_CREDIT) {
      return BankTransactionTypes.CREDIT;
    }

    if (type == Bank1Transaction.TYPE_DEBIT) {
      return BankTransactionTypes.DEBIT;
    }

    throw new InvalidTransactionTypeException();
  }
}
