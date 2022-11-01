package com.bofa.service;

import com.bofa.model.Account;
import com.bofa.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    Transaction makeTransfer(Account sender, Account receiver, BigDecimal amount,
                             Date creationDate, String message);

    List<Transaction> findAllTransaction();

    List<Transaction> lastTransactions();

    List<Transaction> findTransactionListById(UUID id);
}
