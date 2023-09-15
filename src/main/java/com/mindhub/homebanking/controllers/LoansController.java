package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import com.mindhub.homebanking.utils.SumPercentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
            return new ResponseEntity<>("The number of installments is incorrect or empty", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getNumberAccount().isBlank()){
            return new ResponseEntity<>("The account number cannot be empty", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount()<=0){
            return new ResponseEntity<>("The amount cannot be 0 or less", HttpStatus.FORBIDDEN);
        }
        Client client= clientService.findByEmail(authentication.getName());
        Loan loan= loanService.findById(loanApplicationDTO.getId());
        Account account= accountService.findByNumberAndClient(loanApplicationDTO.getNumberAccount(),client);
        if (loan==null){
            return new ResponseEntity<>("There is no loan", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount()> loan.getMaxAmount()){
            return new ResponseEntity<>("Incorrect amount", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayment().contains(loanApplicationDTO.getPayment())){
            return new ResponseEntity<>("Wrong number of installments", HttpStatus.FORBIDDEN);
        }
        if (account==null){
            return new ResponseEntity<>("Account not found or does not belong to the selected customer", HttpStatus.FORBIDDEN);
        }
        double amountWhitInterest= SumPercentage.sumOfInterest(loanApplicationDTO.getAmount());
        ClientLoan clientLoan= new ClientLoan(amountWhitInterest, loanApplicationDTO.getPayment());
        if(clientLoanService.existsByClientAndLoan(client,loan)){
            return new ResponseEntity<>("I already requested this loan",HttpStatus.FORBIDDEN);
        }
        Transaction transactionCredit= new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName()+" approved", LocalDateTime.now(),account.getBalance()+loanApplicationDTO.getAmount());
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
}
