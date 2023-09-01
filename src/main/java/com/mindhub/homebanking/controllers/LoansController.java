package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoansController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping("/loans")
    public Set<LoanDTO> loanDTO(){
        Set<LoanDTO> loanDTOSet= loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toSet());
        return loanDTOSet;
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        if (authentication==null){
            return new ResponseEntity<>("Session Expired", HttpStatus.FORBIDDEN);
        }
        Client client= clientRepository.findByEmail(authentication.getName());
        Loan loan= loanRepository.findById(loanApplicationDTO.getId());
        Account account= accountRepository.findByNumber(loanApplicationDTO.getNumberAccount());
        if (loan==null){
            return new ResponseEntity<>("No existe el prestamo", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount()> loan.getMaxAmount() || loanApplicationDTO.getAmount()<=0){
            return new ResponseEntity<>("Monto incorrecto", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayment().contains(loanApplicationDTO.getPayment())){
            return new ResponseEntity<>("Numero de cuotas equivocado", HttpStatus.FORBIDDEN);
        }
        if (account==null || !client.getAccounts().contains(account)){
            return new ResponseEntity<>("Cuenta no encontrada o no pertenece al cliente seleccionado", HttpStatus.FORBIDDEN);
        }
        double amountWhitInterest= sumOfInterest(loanApplicationDTO.getAmount());
        ClientLoan clientLoan= new ClientLoan(amountWhitInterest, loanApplicationDTO.getPayment());
        Transaction transactionCredit= new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), "loan approved", LocalDateTime.now());
        transactionRepository.save(transactionCredit);
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        account.addTransactions(transactionCredit);
        account.setBalance(account.getBalance()+loanApplicationDTO.getAmount());
        accountRepository.save(account);
        clientLoanRepository.save(clientLoan);
        clientRepository.save(client);
        return new ResponseEntity<>("Loan Success", HttpStatus.CREATED);
    }

    private double sumOfInterest(double amount){
        return  amount+(amount*0.20);
    }
}
