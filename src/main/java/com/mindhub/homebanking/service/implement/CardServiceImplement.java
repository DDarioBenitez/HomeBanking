package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card findByTypeAndColorAndClient(CardType type, CardColor color, Client client) {
        return cardRepository.findByTypeAndColorAndClient(type,color,client);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card findByNumberAndClient(String numberCard, Client client) {
        return  cardRepository.findByNumberAndClient(numberCard, client);
    }

    @Override
    public Card findByNumberAndClientAndCvv(String numberCard, Client client, int cvv) {
        return cardRepository.findByNumberAndClientAndCvv(numberCard,client,cvv);
    }

    @Override
    public List<Card> findAllByActiveAndClient(boolean active, Client client) {
        return cardRepository.findAllByActiveAndClient(active,client);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
