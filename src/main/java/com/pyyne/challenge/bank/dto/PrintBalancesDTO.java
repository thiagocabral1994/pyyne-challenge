package com.pyyne.challenge.bank.dto;

import java.util.Map;
import java.util.Objects;

import com.pyyne.challenge.bank.model.BankAccountBalance;

public class PrintBalancesDTO {
  private final Map<String, BankAccountBalance> balances;
  
  public PrintBalancesDTO(Map<String, BankAccountBalance> balanceMapping) {
    this.balances = balanceMapping;
  }

  public Map<String, BankAccountBalance> getBalances() {
    return balances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PrintBalancesDTO that = (PrintBalancesDTO) o;
    return Objects.equals(balances, that.balances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(balances);
  }
}
