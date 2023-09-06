package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoansController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/loans")
    public List<LoanDTO> loanDTO(){
        List<LoanDTO> loanDTOSet= loanService.getAllLoansDTO();
        return loanDTOSet;
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        if (authentication==null){
            return new ResponseEntity<>("Session Expired", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getId()<=0){
            return new ResponseEntity<>("La cantidad de cuotas es incorrecta o esta vacio", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getNumberAccount().isBlank()){
            return new ResponseEntity<>("El numero de cuenta no puede estar vacio", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount()<=0){
            return new ResponseEntity<>("El monto no puede ser 0 o menor", HttpStatus.FORBIDDEN);
        }
        Client client= clientService.findByEmail(authentication.getName());
        Loan loan= loanService.findById(loanApplicationDTO.getId());
        Account account= accountService.findByNumberAndClient(loanApplicationDTO.getNumberAccount(),client);
        if (loan==null){
            return new ResponseEntity<>("No existe el prestamo", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount()> loan.getMaxAmount()){
            return new ResponseEntity<>("Monto incorrecto", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayment().contains(loanApplicationDTO.getPayment())){
            return new ResponseEntity<>("Numero de cuotas equivocado", HttpStatus.FORBIDDEN);
        }
        if (account==null){
            return new ResponseEntity<>("Cuenta no encontrada o no pertenece al cliente seleccionado", HttpStatus.FORBIDDEN);
        }
        double amountWhitInterest= sumOfInterest(loanApplicationDTO.getAmount());
        ClientLoan clientLoan= new ClientLoan(amountWhitInterest, loanApplicationDTO.getPayment());
        if(clientLoanService.existsByClientAndLoan(client,loan)){
            return new ResponseEntity<>("Ya pidio este prestamo",HttpStatus.FORBIDDEN);
        }
        Transaction transactionCredit= new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName()+" approved", LocalDateTime.now());
        transactionService.saveTransaction(transactionCredit);
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        account.addTransactions(transactionCredit);
        account.setBalance(account.getBalance()+loanApplicationDTO.getAmount());
        accountService.saveAccount(account);
        clientLoanService.saveClientLoan(clientLoan);
        clientService.saveClient(client);
        loanService.saveLoan(loan);
        return new ResponseEntity<>("Loan Success", HttpStatus.CREATED);
    }

    private double sumOfInterest(double amount){
        return  amount+(amount*0.20);
    }
}
