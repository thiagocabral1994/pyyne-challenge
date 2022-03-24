package com.pyyne.challenge.bank.adapter;

import com.bank1.integration.Bank1AccountSource;
import com.bank1.integration.Bank1Transaction;
import com.pyyne.challenge.bank.constant.BankTransactionTypes;
import com.pyyne.challenge.bank.exception.InvalidBankAccountArgumentsException;
import com.pyyne.challenge.bank.exception.InvalidTransactionTypeException;
import com.pyyne.challenge.bank.model.BankAccountBalance;

import com.pyyne.challenge.bank.model.BankAccountTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bank 1 Account Adapter Test")
public class Bank1AccountSourceAdapterTest {

    private final Bank1AccountSource bank1AccountSource;
    private final Bank1AccountSourceAdapter bank1AccountSourceAdapter;

    public Bank1AccountSourceAdapterTest() {
     bank1AccountSource = Mockito.mock(Bank1AccountSource.class);
     bank1AccountSourceAdapter = new Bank1AccountSourceAdapter(bank1AccountSource);
    }

    @Test
    @DisplayName("Should successfully find balance with valid ID")
    public void shouldSuccessfullyFindBalanceWithValidId() {
        long accountId = 1;
        double balance = 100d;
        String currency = "USD";

        Mockito.lenient().when(bank1AccountSource.getAccountBalance(accountId)).thenReturn(balance);
        Mockito.lenient().when(bank1AccountSource.getAccountCurrency(accountId)).thenReturn(currency);

        assertEquals(this.bank1AccountSourceAdapter.getBalance(accountId), new BankAccountBalance(balance, currency));
    }

    @Test
    @DisplayName("Should throw error when finding balance with invalid ID")
    public void shouldThrowErrorWhenFindingBalanceWithInvalidId() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank1AccountSourceAdapter.getBalance(null));
    }

    @Test
    @DisplayName("Should successfully find transactions with valid ID")
    public void shouldSuccessfullyFindTransactionsWithValidId() {
        long accountId = 1;
        Date fromDate = new Date();
        Date toDate = new Date();

        List<Bank1Transaction> bank1Transactions = new ArrayList<>();
        bank1Transactions.add(new Bank1Transaction(100d, Bank1Transaction.TYPE_DEBIT, "Transaction 1"));
        bank1Transactions.add(new Bank1Transaction(1000d, Bank1Transaction.TYPE_CREDIT, "Transaction 2"));

        Mockito.lenient().when(bank1AccountSource.getTransactions(accountId, fromDate, toDate)).thenReturn(bank1Transactions);

        List<BankAccountTransaction> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(new BankAccountTransaction(100d, BankTransactionTypes.DEBIT, "Transaction 1"));
        expectedTransactions.add(new BankAccountTransaction(1000d, BankTransactionTypes.CREDIT, "Transaction 2"));

        assertEquals(this.bank1AccountSourceAdapter.getTransactions(accountId, fromDate, toDate), expectedTransactions);
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid ID")
    public void shouldThrowErrorWhenFindingTransactionsWithInvalidId() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank1AccountSourceAdapter.getTransactions(null, new Date(), new Date()));
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid start date")
    public void shouldThrowErrorWhenFindingTransactionsWithStartDate() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank1AccountSourceAdapter.getTransactions(1L, null, new Date()));
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid end date")
    public void shouldThrowErrorWhenFindingTransactionsWithEndDate() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank1AccountSourceAdapter.getTransactions(1L, new Date(), null));
    }
  
    @Test
    @DisplayName("Should throw error when invalid transaction type is provided")
    public void shouldThrowErrorWhenInvalidTransactionTypeIsProvided() {
        long accountId = 1;
        Date fromDate = new Date();
        Date toDate = new Date();
    
        List<Bank1Transaction> bank1Transactions = new ArrayList<>();
        bank1Transactions.add(new Bank1Transaction(1000d, 5, "Transaction 1"));

        Mockito.lenient().when(bank1AccountSource.getTransactions(accountId, fromDate, toDate)).thenReturn(bank1Transactions);

        assertThrows(InvalidTransactionTypeException.class, () -> this.bank1AccountSourceAdapter.getTransactions(accountId, fromDate, toDate));
    }
}
