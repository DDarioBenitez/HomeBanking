package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionsController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String originAccount,@RequestParam String destinyAccount, Authentication authentication){
        if (authentication!=null){
            if (amount<=0){
                return new ResponseEntity<>("The amount cannot be empty", HttpStatus.FORBIDDEN);
            }
            if (description.isBlank()){
                return new ResponseEntity<>("The description cannot be empty", HttpStatus.FORBIDDEN);
            } if (destinyAccount.isBlank()){
                return new ResponseEntity<>("The destiny account cannot be empty", HttpStatus.FORBIDDEN);
            } if (originAccount.isBlank()){
                return new ResponseEntity<>("The origin account cannot be empty", HttpStatus.FORBIDDEN);
            }

            Client clientOrigin= clientService.findByEmail(authentication.getName());
            Account accountOrigin= accountService.findByNumberAndClient(originAccount,clientOrigin);
            Account accountDestiny= accountService.findByNumber(destinyAccount);

            if (accountOrigin==null){
                return new ResponseEntity<>("cuenta no existe", HttpStatus.FORBIDDEN);
            }
            if (accountOrigin.getBalance()<amount){
                return new ResponseEntity<>("El monto supera el balance actual", HttpStatus.FORBIDDEN);
            }
            if (accountOrigin.equals(accountDestiny)){
                return new ResponseEntity<>("No se pueden hacer transacciones entre dos cuentas iguales", HttpStatus.FORBIDDEN);
            }

            Transaction debitTransaction=new Transaction(TransactionType.DEBIT, amount,description, LocalDateTime.now());
            Transaction creditTransaction= new Transaction(TransactionType.CREDIT, amount, description,LocalDateTime.now());
            transactionService.saveTransaction(debitTransaction);
            transactionService.saveTransaction(creditTransaction);

            accountOrigin.setBalance(accountOrigin.getBalance()-amount);
            accountDestiny.setBalance(accountDestiny.getBalance()+amount);
            accountOrigin.addTransactions(debitTransaction);
            accountDestiny.addTransactions(creditTransaction);
            accountService.saveAccount(accountOrigin);
            accountService.saveAccount(accountDestiny);

            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("Session expired", HttpStatus.FORBIDDEN);
        }
    }
}
