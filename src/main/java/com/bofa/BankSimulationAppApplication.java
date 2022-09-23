package com.bofa;

import com.bofa.enums.AccountType;
import com.bofa.model.Account;
import com.bofa.service.AccountService;
import com.bofa.service.TransactionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class BankSimulationAppApplication {

    public static void main(String[] args) {

        ApplicationContext container = SpringApplication.run(BankSimulationAppApplication.class, args);

        AccountService accountService = container.getBean(AccountService.class);
        TransactionService transactionService = container.getBean(TransactionService.class);

        Account sender = accountService.createNewAccount(BigDecimal.valueOf(7500), new Date(), AccountType.CHECKING, 1L);
        Account receiver = accountService.createNewAccount(BigDecimal.valueOf(2500), new Date(), AccountType.CHECKING, 12L);

        accountService.listAllAccounts().forEach(System.out::println);

        transactionService.makeTransfer(sender, receiver, new BigDecimal(40), new Date(), "Transaction 1");

        System.out.println(transactionService.findAllTransaction().get(0));

        accountService.listAllAccounts().forEach(System.out::println);

    }

}
