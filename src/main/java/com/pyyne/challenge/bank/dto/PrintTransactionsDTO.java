package com.pyyne.challenge.bank.dto;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import com.pyyne.challenge.bank.model.BankAccountTransaction;

public class PrintTransactionsDTO {
  private final Map<String, Collection<BankAccountTransaction>> transactions;
  
  public PrintTransactionsDTO(Map<String, Collection<BankAccountTransaction>> transactionMapping) {
    this.transactions = transactionMapping;
  }

  public Map<String, Collection<BankAccountTransaction>> getTransactions() {
    return transactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PrintTransactionsDTO that = (PrintTransactionsDTO) o;
    return Objects.equals(transactions, that.transactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactions);
  }
}
