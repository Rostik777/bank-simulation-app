package com.bofa.service.impl;

import com.bofa.enums.AccountType;
import com.bofa.exception.AccountOwnershipException;
import com.bofa.exception.BadRequestException;
import com.bofa.exception.BalanceNotSufficientException;
import com.bofa.exception.UnderConstructionException;
import com.bofa.model.Account;
import com.bofa.model.Transaction;
import com.bofa.repository.AccountRepository;
import com.bofa.repository.TransactionRepository;
import com.bofa.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransferServiceImpl implements TransactionService {
    @Value("${under_construction}")
    private boolean underConstruction;
    AccountRepository accountRepository;
    TransactionRepository transactionRepository;

    public TransferServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction makeTransfer(Account sender, Account receiver, BigDecimal amount, Date creationDate, String message) {
        if(!underConstruction) {
            validateAccount(sender, receiver);
            checkAccountOwnership(sender, receiver);
            executeBalanceAndUpdateIfRequired(amount, sender, receiver);

            Transaction transaction = Transaction.builder()
                    .amount(amount)
                    .sender(sender.getId())
                    .receiver(receiver.getId())
                    .creationDate(creationDate)
                    .message(message)
                    .build();
            return transactionRepository.save(transaction);
        } else {
            throw new UnderConstructionException("App is under construction, try again later");
        }
    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, Account sender, Account receiver) {
        if(checkSenderBalance(sender, amount)) {
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));
        } else {
            throw new BalanceNotSufficientException("Balance is not enough for this transfer");
        }
    }

    private boolean checkSenderBalance(Account sender, BigDecimal amount) {
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >=0;
    }

    private void checkAccountOwnership(Account sender, Account receiver) {
        if((sender.getAccountType().equals(AccountType.SAVING)
                || receiver.getAccountType().equals(AccountType.SAVING)) && !sender.getUserId().equals(receiver.getUserId()))
        {
            throw new AccountOwnershipException("If one of the accounts is saving, userId must be the same");
        }
    }

    private void validateAccount(Account sender, Account receiver) {
        /*
            -if any of the account is null
            -if account ids are the same(same account)
            -if the account exist in the database(repository)
        */
        if(sender == null || receiver == null) {
            throw new BadRequestException("Sender or Receiver cannot be null");
        }
        if(sender.getId().equals(receiver.getId())) {
            throw new BadRequestException("Sender account needs to be different than receiver");
        }
        findAccountById(sender.getId());
        findAccountById(receiver.getId());
    }

    private Account findAccountById(UUID id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Transaction> findAllTransaction() {

        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> lastTransactions() {
        return findAllTransaction()
                .stream()
                .sorted(Comparator.comparing(Transaction::getCreationDate).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findTransactionListById(UUID id) {
        return findAllTransaction().stream().filter(t -> t.getSender().equals(id) || t.getReceiver().equals(id)).collect(Collectors.toList());
    }
}
