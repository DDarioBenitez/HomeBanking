package com.mindhub.homebanking.utils;

public final class SumPercentage {
    public static double sumOfInterest(double amount, double interest){
       return amount+(amount * interest / 100);
    }
}
