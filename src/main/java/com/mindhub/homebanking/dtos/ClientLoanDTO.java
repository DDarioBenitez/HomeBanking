package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private double amount;
    private int payment;
    private long loan;
    private String name;
    public ClientLoanDTO(){

    }

    public ClientLoanDTO (ClientLoan clientLoan){
        this.id=clientLoan.getId();
        this.amount=clientLoan.getAmount();
        this.payment=clientLoan.getPayment();
        this.loan=clientLoan.getLoan().getId();
        this.name=clientLoan.getLoan().getName();
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayment() {
        return payment;
    }

    public long getLoan() {
        return loan;
    }

    public String getName() {
        return name;
    }
}
