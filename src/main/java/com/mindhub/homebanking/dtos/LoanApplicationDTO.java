package com.mindhub.homebanking.dtos;


public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payment;
    private String numberAccount;

    public LoanApplicationDTO(){

    }
    public LoanApplicationDTO(long id, String numberAccount, double amount,int payment){
        this.id= id;
        this.numberAccount=numberAccount;
        this.amount=amount;
        this.payment=payment;
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

    public String getNumberAccount() {
        return numberAccount;
    }
}
