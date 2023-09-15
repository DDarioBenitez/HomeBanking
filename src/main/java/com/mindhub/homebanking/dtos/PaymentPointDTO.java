package com.mindhub.homebanking.dtos;

public class PaymentPointDTO {
    private String cardNumber;
    private int cvv;
    private double amount;
    private String description;

    public PaymentPointDTO(){}
    public PaymentPointDTO(String cardNumber, int cvv, double amount, String description) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
