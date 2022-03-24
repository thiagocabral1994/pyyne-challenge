package com.pyyne.challenge.bank.service;

import com.pyyne.challenge.bank.adapter.BankAccountSourceAdapter;
import com.pyyne.challenge.bank.constant.BankTransactionTypes;
import com.pyyne.challenge.bank.dto.PrintBalancesDTO;
import com.pyyne.challenge.bank.dto.PrintTransactionsDTO;
import com.pyyne.challenge.bank.exception.InvalidBankAccountArgumentsException;
import com.pyyne.challenge.bank.model.BankAccountBalance;
import com.pyyne.challenge.bank.model.BankAccountTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bank Service Test")
public class BankServiceTest {

    private final BankAccountSourceAdapter bankA;
    private final BankAccountSourceAdapter bankB;
    private final BankService bankService;

    public BankServiceTest() {
        this.bankService = new BankService();
        this.bankA = Mockito.mock(BankAccountSourceAdapter.class);
        this.bankB = Mockito.mock(BankAccountSourceAdapter.class);

        Map<String, BankAccountSourceAdapter> banks = new HashMap<>();
        banks.put("bankA", this.bankA);
        banks.put("bankB", this.bankB);

        ReflectionTestUtils.setField(this.bankService, "banks", banks);
    }

    @Test
    @DisplayName("Should successfully find balance with valid ID")
    public void shouldSuccessfullyFindBalances() {
        long accountId = 1;

        BankAccountBalance balanceBankA = new BankAccountBalance(100d, "BRL");
        BankAccountBalance balanceBankB = new BankAccountBalance(200d, "BRL");

        Mockito.lenient().when(this.bankA.getBalance(accountId)).thenReturn(balanceBankA);
        Mockito.lenient().when(this.bankB.getBalance(accountId)).thenReturn(balanceBankB);

        Map<String, BankAccountBalance> expectedMapping = new HashMap<>();
        expectedMapping.put("bankA", balanceBankA);
        expectedMapping.put("bankB", balanceBankB);

        assertEquals(this.bankService.listEveryBankAccountBalance(accountId), new PrintBalancesDTO(expectedMapping));
    }

    @Test
    @DisplayName("Should throw exception when finding balance with invalid ID")
    public void shouldThrowExceptionWhenFindingBalanceWithInvalidId() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bankService.listEveryBankAccountBalance(null));
    }

    @Test
    @DisplayName("Should successfully find transactions with valid ID")
    public void shouldSuccessfullyFindTransactions() {
        long accountId = 1;
        Date fromDate = new Date();
        Date toDate = new Date();

        Collection<BankAccountTransaction> bankATransactions = new ArrayList<>();
        bankATransactions.add(new BankAccountTransaction(100d, BankTransactionTypes.DEBIT, "Bank A Transaction 1"));
        bankATransactions.add(new BankAccountTransaction(200d, BankTransactionTypes.CREDIT, "Bank A Transaction 2"));

        Collection<BankAccountTransaction> bankBTransactions = new ArrayList<>();
        bankBTransactions.add(new BankAccountTransaction(1000d, BankTransactionTypes.CREDIT, "Bank B Transaction 1"));
        bankBTransactions.add(new BankAccountTransaction(500d, BankTransactionTypes.DEBIT, "Bank B Transaction 2"));

        Mockito.lenient().when(this.bankA.getTransactions(accountId, fromDate, toDate)).thenReturn(bankATransactions);
        Mockito.lenient().when(this.bankB.getTransactions(accountId, fromDate, toDate)).thenReturn(bankBTransactions);

        Map<String, Collection<BankAccountTransaction>> expectedMapping = new HashMap<>();
        expectedMapping.put("bankA", bankATransactions);
        expectedMapping.put("bankB", bankBTransactions);

        assertEquals(this.bankService.listEveryBankAccountTransactions(accountId, fromDate, toDate), new PrintTransactionsDTO(expectedMapping));
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid ID")
    public void shouldThrowErrorWhenFindingTransactionsWithInvalidId() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bankService.listEveryBankAccountTransactions(null, new Date(), new Date()));
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid start date")
    public void shouldThrowErrorWhenFindingTransactionsWithStartDate() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bankService.listEveryBankAccountTransactions(1L, null, new Date()));
    }

    @Test
    @DisplayName("Should throw error when finding transactions with invalid end date")
    public void shouldThrowErrorWhenFindingTransactionsWithEndDate() {
        assertThrows(InvalidBankAccountArgumentsException.class, () -> this.bankService.listEveryBankAccountTransactions(1L, new Date(), null));
    }
}
