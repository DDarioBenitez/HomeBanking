package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentPointDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CardService cardService;

    @Transactional
    @PostMapping("/payment_point")
    public ResponseEntity<Object> paymentPoint(@RequestBody PaymentPointDTO paymentPointDTO) {

        if (paymentPointDTO.getCardNumber().isBlank()) {
            return new ResponseEntity<>("Numero de tarjeta no puede estar en blanco", HttpStatus.FORBIDDEN);
        }
        if (Integer.toString(paymentPointDTO.getCvv()).length() < 3 || Integer.toString(paymentPointDTO.getCvv()).isBlank()) {
            return new ResponseEntity<>("Cvv incorrecto, muy corto o en blanco", HttpStatus.FORBIDDEN);
        }
        if (paymentPointDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Monto incorrecto no puede ser 0 o menos que 0", HttpStatus.FORBIDDEN);
        }
        if (paymentPointDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("Descripcion incorrecta, no puede estar vacio", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByCardNumber(paymentPointDTO.getCardNumber());
        Card card = cardService.findByNumberAndClientAndCvv(paymentPointDTO.getCardNumber(), client, paymentPointDTO.getCvv());
        if (client == null) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.FORBIDDEN);
        }
        if (card == null) {
            return new ResponseEntity<>("Tarjeta no encontrada", HttpStatus.FORBIDDEN);
        }
        if (LocalDate.now().isEqual(card.getThruDate())){
            return new ResponseEntity<>("La tarjeta vencio", HttpStatus.FORBIDDEN);
        }
        Set<Account> accounts = client.getAccounts().stream().filter(Account::isActive).collect(Collectors.toSet());
        if (accounts.isEmpty()) {
            return new ResponseEntity<>("No tiene ninguna cuenta asociada", HttpStatus.FORBIDDEN);
        }
        Account account = null;
        for (Account acc : accounts) {
            if (acc.getBalance() >= paymentPointDTO.getAmount()) {
                account = acc;
                break;
            }
        }
        if (account == null) {
            return new ResponseEntity<>("Balance insuficiente", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.DEBIT, paymentPointDTO.getAmount(), paymentPointDTO.getDescription(), LocalDateTime.now(),account.getBalance() - paymentPointDTO.getAmount());
        transaction.setAccount(account);
        account.setBalance(account.getBalance() - paymentPointDTO.getAmount());
        account.addTransactions(transaction);
        transactionService.saveTransaction(transaction);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
    }

}
