package com.bofa.service.impl;

import com.bofa.enums.AccountStatus;
import com.bofa.enums.AccountType;
import com.bofa.model.Account;
import com.bofa.repository.AccountRepository;
import com.bofa.service.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createNewAccount(BigDecimal balance, Date creationDate, AccountType accountType, Long userId) {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .accountType(accountType)
                .balance(balance)
                .creationDate(creationDate)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return accountRepository.save(account);
    }

    @Override
    public List<Account> listAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(UUID id) {
        accountRepository.findById(id).setAccountStatus(AccountStatus.DELETED);
    }

    @Override
    public Account retrieveById(UUID sender) {
        return accountRepository.findById(sender);
    }
}
