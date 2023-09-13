package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;
import java.util.Set;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
