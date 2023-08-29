package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.mindhub.homebanking.models.CardType.CCREDIT;
import static com.mindhub.homebanking.models.CardType.CDEBIT;
import static com.mindhub.homebanking.utils.RandomNumberGenerator.*;


@RestController
@RequestMapping ("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createdCard(@RequestParam String type, @RequestParam String color,Authentication authentication) {//servlet para reguistrar una nueva tarjeta, pide por parametros el typo y el color ademas del usuario autenticado
        if (authentication != null) {//compruebo que el usuario estee autenticado
            Client client = clientRepository.findByEmail(authentication.getName());//busco en la base de datos el cliente en base a su email y lo guardo en una variable
            CardType cardType = CardType.valueOf(type);
            CardColor cardColor=CardColor.valueOf(color);
            if (cardRepository.findByTypeAndColorAndClient(cardType, cardColor, client)==null) {//compruebo que el cliente tenga menos del numero maximo de tarjeta
                int cvv = cvvGenerator();//genero el cvv
                String numberCard = cardType == CCREDIT ? creditNumberGenerator() : debitNumberGenerator();//genero el numero de la tarjeta en base al tipo de tarjeta que quiere crear el usuario
                while (cardRepository.findByNumber(numberCard) != null) {// verifico que el numero no estee ya reguistrado en la base de datos
                    numberCard = cardType == CCREDIT ? creditNumberGenerator() : debitNumberGenerator();//en caso que exista una tarjeta con el numero generado aleatoriamente genera un nuevo numero
                }
                String cardHolder = client.getFirstName() + " " + client.getLastName();//creo el nombre que va a ir impreso en la tarjeta y lo guardo en una variable
                Card newCard = new Card(cardType, cardColor, LocalDate.now(), cvv, numberCard, cardHolder);// creo una nueva tarjeta
                client.addCard(newCard);//agrego la tarjeta al cliente
                cardRepository.save(newCard);//guardo la tarjeta
                clientRepository.save(client);//guardo el cliente
                return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);//retorno un mensaje de exito y el status created
            } else {//si ya tiene el maximo numero de tarjetas devuevolvo mensaje mas status
                return new ResponseEntity<>("Can't create another card", HttpStatus.FORBIDDEN);
            }
        }else {
            return new ResponseEntity<>("Session expired",HttpStatus.FORBIDDEN);
        }
    }
}
