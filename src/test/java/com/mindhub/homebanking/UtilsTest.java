package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.RandomNumberGenerator;
import com.mindhub.homebanking.utils.SumPercentage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class UtilsTest {
    @Test
    public void getCvv(){
        int cvv= RandomNumberGenerator.cvvGenerator();
        assertEquals(3, String.valueOf(cvv).length());
    }
    @Test
    public  void getNumberAccount(){
        String numberAcc= RandomNumberGenerator.accountNumberGenerator();
        assertEquals(8, numberAcc.length());
    }
    @Test
    public void getCreditNumber(){
        String creditNumber= RandomNumberGenerator.creditNumberGenerator();
        assertEquals(19, creditNumber.length());
        assertEquals("4513", creditNumber.substring(0,4));
    }
    @Test
    public void getDebitNumber(){
        String debitNumber= RandomNumberGenerator.debitNumberGenerator();
        assertEquals(19, debitNumber.length());
        assertEquals("4000", debitNumber.substring(0,4));
    }
    @Test
    public void sumPercentage(){
        double amountWhitPercentage= SumPercentage.sumOfInterest(20000.52, 14);
        assertEquals(20000.52 + (20000.52*0.14), amountWhitPercentage, 0.01);
    }
}
