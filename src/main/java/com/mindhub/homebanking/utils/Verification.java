package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.mindhub.homebanking.utils.RandomNumberGenerator.accountNumberGenerator;


public class Verification {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    public Account accountNew(Account account,String numberAccount) {
//        if (account != null) {
//            String newAccountNumber = accountNumberGenerator();
//            while (accountRepository.findByNumber(newAccountNumber) != null) {
//                newAccountNumber = accountNumberGenerator();
//            }
//            return new Account("VIN" + newAccountNumber, LocalDate.now(), 0);
//        }else{
//          return new Account("VIN"+numberAccount,LocalDate.now(),0);
//        }
//    }
}
