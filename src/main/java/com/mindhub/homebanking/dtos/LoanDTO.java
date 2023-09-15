package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private long id;
    private String name;
    private double maxAmount;
    private List<Double> percentage;
    private List<Integer> payment;

    public LoanDTO(){

    }
    public LoanDTO(String name,double maxAmount,List<Integer> payment,List<Double> percentage){
        this.name=name;
        this.maxAmount=maxAmount;
        this.percentage=percentage;
        this.payment=payment;
    }
    public LoanDTO(Loan loan){
    this.id= loan.getId();
    this.name= loan.getName();
    this.maxAmount= loan.getMaxAmount();
    this.percentage= loan.getPercentage();
    this.payment=loan.getPayment();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Double> getPercentage() {
        return percentage;
    }

    public List<Integer> getPayment() {
        return payment;
    }
}
