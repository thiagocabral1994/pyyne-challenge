package com.pyyne.challenge.bank.adapter;

import java.util.Collection;
import java.util.Date;

import com.pyyne.challenge.bank.model.BankAccountBalance;
import com.pyyne.challenge.bank.model.BankAccountTransaction;

public interface BankAccountSourceAdapter {
  public Collection<BankAccountTransaction> getTransactions(Long accountId, Date fromDate, Date toDate);
  public BankAccountBalance getBalance(Long accountId);
}
