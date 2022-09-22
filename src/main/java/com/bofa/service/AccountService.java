package com.bofa.service;

import com.bofa.enums.AccountType;
import com.bofa.model.Account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AccountService {
    Account createNewAccount(BigDecimal balance, Date creationDate, AccountType accountType, Long userId);

    List<Account> listAllAccounts();

}
