package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private int balance;
    private List<TransactionDTO> transactions;

    public AccountDTO(Account account){
        this.number=account.getNumber();
        this.creationDate=account.getCreationDate();
        this.balance=account.getBalance();
        this.id=account.getId();
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


    public int getBalance() {
        return balance;
    }


    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

}
