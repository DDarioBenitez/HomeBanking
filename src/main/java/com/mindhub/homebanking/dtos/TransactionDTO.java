package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
    private double balanceAccount;

    public TransactionDTO(Transaction transaction){
        this.id= transaction.getId();
        this.amount=transaction.getAmount();
        this.description= transaction.getDescription();
        this.date=transaction.getDate();
        this.type=transaction.getType();
        this.balanceAccount=transaction.getBalanceAccount();
    }

    public Long getId(){
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public double getBalanceAccount() {
        return balanceAccount;
    }
}
