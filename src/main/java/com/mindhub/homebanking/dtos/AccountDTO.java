package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private AccountType type;
    private List<TransactionDTO> transactions;

    public AccountDTO(Account account){
        this.number=account.getNumber();
        this.creationDate=account.getCreationDate();
        this.balance=account.getBalance();
        this.id=account.getId();
        this.type=account.getType();
        this.transactions=account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }


    public double getBalance() {
        return balance;
    }


    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountType getType() {
        return type;
    }
}
