package com.mindhub.homebanking.dtos;


public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payment;
    private double percentage;
    private String numberAccount;

    public LoanApplicationDTO(){

    }
    public LoanApplicationDTO(long id, String numberAccount, double amount,int payment,double percentage){
        this.id= id;
        this.numberAccount=numberAccount;
        this.amount=amount;
        this.payment=payment;
        this.percentage=percentage;
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

    public double getPercentage() {
        return percentage;
    }

    public String getNumberAccount() {
        return numberAccount;
    }
}
