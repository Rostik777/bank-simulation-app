package com.bofa.controller;

import com.bofa.enums.AccountType;
import com.bofa.model.Account;
import com.bofa.repository.AccountRepository;
import com.bofa.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.UUID;

@Controller
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/index")
    public String getAccountList(Model model) {
        model.addAttribute("accountList", accountService.listAllAccounts());

        return "account/index";
    }

    @GetMapping("/create-account")
    public String getCreateForm(Model model) {
        model.addAttribute("account", Account.builder().build());
        model.addAttribute("accountTypes", AccountType.values());
        return "account/create-account";
    }

    @PostMapping("/create")
    public String createAccount(Account account) {
        accountService.createNewAccount(account.getBalance(), new Date(), account.getAccountType(), account.getUserId());

        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") UUID id) {
        accountService.deleteAccount(id);

        return "redirect:/index";
    }
}
