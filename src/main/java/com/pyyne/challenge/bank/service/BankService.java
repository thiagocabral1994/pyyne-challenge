package com.pyyne.challenge.bank.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bank1.integration.Bank1AccountSource;
import com.pyyne.challenge.bank.adapter.*;
import com.pyyne.challenge.bank.dto.PrintBalancesDTO;
import com.pyyne.challenge.bank.dto.PrintTransactionsDTO;
import com.pyyne.challenge.bank.exception.InvalidBankAccountArgumentsException;
import com.pyyne.challenge.bank.model.BankAccountBalance;
import com.pyyne.challenge.bank.model.BankAccountTransaction;

import org.springframework.stereotype.Service;

@Service
public class BankService {
  private final Map<String, BankAccountSourceAdapter> banks;

  public BankService() {
    this.banks = new HashMap<>();
    this.banks.put("bank1", new Bank1AccountSourceAdapter(new Bank1AccountSource()));
    this.banks.put("bank2", new Bank2AccountSourceAdapter(new com.bank2.integration.Bank2AccountSource()));
  }
 
  public PrintBalancesDTO listEveryBankAccountBalance(Long accountId) {
    if(accountId == null) {
      throw new InvalidBankAccountArgumentsException();
    }

    Map<String, BankAccountBalance> accountBalances = this.mapBalances(accountId);

    return new PrintBalancesDTO(accountBalances);
  }

  private Map<String, BankAccountBalance> mapBalances(Long accountId) {
    Map<String, BankAccountBalance> accountBalances = new HashMap<>();

    for (Map.Entry<String, BankAccountSourceAdapter> entry : this.banks.entrySet()) {
      String bankKey = entry.getKey();
      BankAccountSourceAdapter bankSource = entry.getValue();

      accountBalances.put(bankKey, bankSource.getBalance(accountId));
    }
    return accountBalances;
  }

  public PrintTransactionsDTO listEveryBankAccountTransactions(Long accountId, Date fromDate, Date toDate) {
    if(accountId == null || fromDate == null || toDate == null) {
      throw new InvalidBankAccountArgumentsException();
    }

    Map<String, Collection<BankAccountTransaction>> accountTransactions = this.mapTransactionLists(accountId, fromDate, toDate);

    return new PrintTransactionsDTO(accountTransactions);
  }

  private Map<String, Collection<BankAccountTransaction>> mapTransactionLists(Long accountId, Date fromDate, Date toDate) {
    Map<String, Collection<BankAccountTransaction>> accountTransactions = new HashMap<>();

    for (Map.Entry<String, BankAccountSourceAdapter> entry : this.banks.entrySet()) {
      String bankKey = entry.getKey();
      BankAccountSourceAdapter bankSource = entry.getValue();

      accountTransactions.put(bankKey, bankSource.getTransactions(accountId, fromDate, toDate));
    }

    return accountTransactions;
  }
}
