package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import com.mindhub.homebanking.utils.SumPercentage;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionAdapterTransactionManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
        int indexPayment= loan.getPayment().indexOf(loanApplicationDTO.getPayment());
        int indexPercentage= loan.getPercentage().indexOf(loanApplicationDTO.getPercentage());
        if (indexPercentage != indexPayment){
            return new ResponseEntity<>("La couta no corresponde con el interes", HttpStatus.FORBIDDEN);
        }
        if (account==null){
            return new ResponseEntity<>("Account not found or does not belong to the selected customer", HttpStatus.FORBIDDEN);
        }

        double amountWhitInterest= SumPercentage.sumOfInterest(loanApplicationDTO.getAmount(),loanApplicationDTO.getPercentage());
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

    @Transactional
    @PatchMapping("/loans/payment")
    public ResponseEntity<Object> paymentLoan(@RequestParam String loanName,@RequestParam String numberAcc,@RequestParam double amount, Authentication authentication){
        if (loanName.isEmpty()){
            return new ResponseEntity<>("Name incorrecto", HttpStatus.FORBIDDEN);
        }
        if (numberAcc.isEmpty()){
            return new ResponseEntity<>("Numero de cuenta vacio", HttpStatus.FORBIDDEN);
        }
        if (amount<=0){
            return new ResponseEntity<>("Monto incorrecto", HttpStatus.FORBIDDEN);
        }
        Client client= clientService.findByEmail(authentication.getName());
        if (client==null){
            return new ResponseEntity<>("Session expirada o cliente no existe", HttpStatus.FORBIDDEN);
        }
        Account account= accountService.findByNumberAndClient(numberAcc,client);
        if (account==null){
            return new ResponseEntity<>("Cuenta incorrecta", HttpStatus.FORBIDDEN);
        }
        if (amount>account.getBalance()){
            return new ResponseEntity<>("Balance insuficiente", HttpStatus.FORBIDDEN);
        }
        Loan loan= loanService.findByName(loanName);
        if (loan==null){
            return new ResponseEntity<>("Loan no existe", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = client.getClientLoans().stream()
                .filter(loanC-> loanC.getLoan().getName().equals(loanName))
                .findFirst()
                .orElse(null);
        if (clientLoan==null){
            return new ResponseEntity<>("No tienes un prestamo con ese nombre", HttpStatus.FORBIDDEN);
        }
        if (amount<clientLoan.getPaymentAmount()){
            return new ResponseEntity<>("Monto insuficiente", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.DEBIT,amount,"Payment "+clientLoan.getLoan().getName()+" Loan",LocalDateTime.now(), account.getBalance()-amount);
        transaction.setBalanceAccount(account.getBalance()-amount);
        transaction.setAccount(account);
        transactionService.saveTransaction(transaction);
        account.setBalance(account.getBalance()-amount);
        account.addTransactions(transaction);
        accountService.saveAccount(account);
        clientLoan.setRemainingAmount(amount);
        clientLoan.setPayment(clientLoan.getPayment()-1);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Success", HttpStatus.FORBIDDEN);
    }
}
