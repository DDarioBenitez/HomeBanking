package com.mindhub.homebanking.service;


import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getAllLoansDTO();
    List<Loan> getAllLoans();
    Loan findById(long id);
    Loan findByName(String name);
    void saveLoan(Loan loan);
}
