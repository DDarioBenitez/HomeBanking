package com.mindhub.homebanking.utils;

public final class TransactionUtil {

    public static double creditTransaction(double accountBalance, double amount){
        return accountBalance+amount;
    }
    public static double debitTransaction(double accountBalance, double amount){
        return accountBalance-amount;
    }
}
