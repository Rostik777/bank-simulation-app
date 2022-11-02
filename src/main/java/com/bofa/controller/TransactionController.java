package com.bofa.controller;

import com.bofa.model.Account;
import com.bofa.model.Transaction;
import com.bofa.service.AccountService;
import com.bofa.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

@Controller
public class TransactionController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }


    @GetMapping("/make-transfer")
    public String makeTransfer(Model model) {
        model.addAttribute("accounts", accountService.listAllAccounts());
        model.addAttribute("transaction", Transaction.builder().build());
        model.addAttribute("lastTransactions", transactionService.lastTransactions());

        return "/transaction/make-transfer";
    }

    @PostMapping("/transfer")
    public String postMakeTransfer(@Valid @ModelAttribute("transaction") Transaction transaction, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accounts", accountService.listAllAccounts());
            return "/transaction/make-transfer";
        }

        Account sender = accountService.retrieveById(transaction.getSender());
        Account receiver = accountService.retrieveById(transaction.getReceiver());
        transactionService.makeTransfer(sender, receiver, transaction.getAmount(), new Date(), transaction.getMessage());

        return "redirect:/make-transfer";
    }

    @GetMapping("/transaction/{id}")
    public String getAccountTransactions(@PathVariable("id") UUID id, Model model) {
        System.out.println(id);
        System.out.println(transactionService.findAllTransaction());
        System.out.println(transactionService.findTransactionListById(id));
        model.addAttribute("allTransactions", transactionService.findTransactionListById(id));

        return "/transaction/transactions";
    }
}
