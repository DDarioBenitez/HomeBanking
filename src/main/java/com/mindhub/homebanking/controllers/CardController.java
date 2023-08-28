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
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardColor.*;
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
    public ResponseEntity<Object> createdCard(@RequestParam String type, @RequestParam String color,Authentication authentication) {
            if (authentication!=null){
                Client client= clientRepository.findByEmail(authentication.getName());
                Set<Card> allCards= client.getCards();
                if (allCards.size()<6){
                    if (type.equals("CREDIT") || type.equals("DEBIT")){
                        CardType cardType= type.equals("CREDIT")? CCREDIT:CDEBIT;
                        Set<Card> cardSet= client.getCards().stream().filter(card -> card.getType()==cardType).collect(Collectors.toSet());
                        if (cardSet.size()<3){
                            if (color.equals("GOLD")|| color.equals( "SILVER") || color.equals("TITANIUM")){
                                CardColor cardColor;
                                if (color.equals("GOLD")){
                                    cardColor=GOLD;
                                } else if (color.equals("SILVER")) {
                                    cardColor=SILVER;
                                } else if (color.equals("TITANIUM")) {
                                    cardColor=TITANIUM;
                                } else {
                                    cardColor = null;
                                }
                                Set<Card> repeatColor= cardSet.stream().filter(card -> card.getColor()==cardColor).collect(Collectors.toSet());
                                if (repeatColor.size()<1){
                                    int cvv= cvvGenerator();
                                    String numberCard= cardType == CCREDIT ? creditNumberGenerator(): debitNumberGenerator();
                                    String cardHolder= client.getFirstName()+" "+ client.getLastName();
                                    Card newCard= new Card(cardType, cardColor, LocalDate.now(), cvv, numberCard,cardHolder);
                                    client.addCard(newCard);
                                    cardRepository.save(newCard);
                                    clientRepository.save(client);
                                    return new ResponseEntity<>("Card creada con exito", HttpStatus.CREATED);
                                }else{
                                    return new ResponseEntity<>("Ya posee una card con este color", HttpStatus.FORBIDDEN);
                                }
                            }else{
                                return new ResponseEntity<>("Color incorrecto", HttpStatus.FORBIDDEN);
                            }
                        }else {
                            return new ResponseEntity<>("Numero maximo de cards alcanzado de este tipo", HttpStatus.FORBIDDEN);
                        }
                }else {
                        return new ResponseEntity<>("Tipo incorrecto", HttpStatus.FORBIDDEN);
                    }
                }else {
                    return new ResponseEntity<>("Maximo de cards alcanzado", HttpStatus.FORBIDDEN);
                }
            }else {
                return new ResponseEntity<>("Sesion expirada", HttpStatus.FORBIDDEN);
            }
    }
}
