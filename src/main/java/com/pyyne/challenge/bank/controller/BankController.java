package com.pyyne.challenge.bank.controller;

import java.util.Date;

import com.pyyne.challenge.bank.dto.PrintBalancesDTO;
import com.pyyne.challenge.bank.dto.PrintTransactionsDTO;
import com.pyyne.challenge.bank.service.BankService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that pulls information form multiple bank integrations and prints them to the console.
 *
 * Created by Par Renyard on 5/12/21.
 */
@RestController
@RequestMapping(path = "/bank", produces = MediaType.APPLICATION_JSON_VALUE)
public class BankController {
    @Autowired
    private BankService bankService;

    @GetMapping("/balances/{id}")
    public ResponseEntity<PrintBalancesDTO> printBalances(@PathVariable("id") long id) {
        PrintBalancesDTO bankAccountBalance =  this.bankService.listEveryBankAccountBalance(id);

        return new ResponseEntity<>(bankAccountBalance, HttpStatus.OK);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<PrintTransactionsDTO> printTransactions(
        @PathVariable("id") long id, 
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate, 
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate
        ) {
        PrintTransactionsDTO bankAccountTransactions =  this.bankService.listEveryBankAccountTransactions(id, fromDate, toDate);

        return new ResponseEntity<>(bankAccountTransactions, HttpStatus.OK);
    }
}
