package com.pyyne.challenge.bank.adapter;

import com.bank2.integration.Bank2AccountBalance;
import com.bank2.integration.Bank2AccountSource;
import com.bank2.integration.Bank2AccountTransaction;
import com.pyyne.challenge.bank.constant.BankTransactionTypes;
import com.pyyne.challenge.bank.exception.InvalidBankAccountArgumentsException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bank 2 Account Adapter Test")
public class Bank2AccountSourceAdapterTest {

    private final Bank2AccountSource bank2AccountSource;
    private final Bank2AccountSourceAdapter bank2AccountSourceAdapter;

    public Bank2AccountSourceAdapterTest() {
        bank2AccountSource = Mockito.mock(Bank2AccountSource.class);
        bank2AccountSourceAdapter = new Bank2AccountSourceAdapter(bank2AccountSource);
    }

    @Test
    @DisplayName("Should successfully find balance with valid ID")
    public void shouldSuccessfullyFindBalanceWithValidId() {
        long accountId = 1;
        double balance = 100d;
        String currency = "USD";

        Mockito.lenient().when(bank2AccountSource.getBalance(accountId)).thenReturn(new Bank2AccountBalance(balance, currency));

        assertEquals(this.bank2AccountSourceAdapter.getBalance(accountId), new BankAccountBalance(balance, currency));
    }

    @Test
    @DisplayName("Should throw error when finding balance with invalid ID")
    public void shouldThrowErrorWhenFindingBalanceWithInvalidId() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank2AccountSourceAdapter.getBalance(null));
    }

    @Test
    @DisplayName("Should successfully find transactions with valid ID")
    public void shouldSuccessfullyFindTransactionsWithValidId() {
        long accountId = 1;
        Date fromDate = new Date();
        Date toDate = new Date();

        List<Bank2AccountTransaction> bank1Transactions = new ArrayList<>();
        bank1Transactions.add(new Bank2AccountTransaction(100d, Bank2AccountTransaction.TRANSACTION_TYPES.DEBIT, "Transaction 1"));
        bank1Transactions.add(new Bank2AccountTransaction(1000d, Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT, "Transaction 2"));

        Mockito.lenient().when(bank2AccountSource.getTransactions(accountId, fromDate, toDate)).thenReturn(bank1Transactions);

        List<BankAccountTransaction> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(new BankAccountTransaction(100d, BankTransactionTypes.DEBIT, "Transaction 1"));
        expectedTransactions.add(new BankAccountTransaction(1000d, BankTransactionTypes.CREDIT, "Transaction 2"));

        assertEquals(this.bank2AccountSourceAdapter.getTransactions(accountId, fromDate, toDate), expectedTransactions);
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid ID")
    public void shouldThrowErrorWhenFindingTransactionsWithInvalidId() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank2AccountSourceAdapter.getTransactions(null, new Date(), new Date()));
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid start date")
    public void shouldThrowErrorWhenFindingTransactionsWithStartDate() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank2AccountSourceAdapter.getTransactions(1L, null, new Date()));
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid end date")
    public void shouldThrowErrorWhenFindingTransactionsWithEndDate() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bank2AccountSourceAdapter.getTransactions(1L, new Date(), null));
    }
}
