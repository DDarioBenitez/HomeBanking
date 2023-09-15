package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface CardService {
    Card findByTypeAndColorAndClient(CardType type, CardColor color, Client client);
    Card findByNumber(String number);
    Card findByNumberAndClient(String numberCard, Client client);
    Card findByNumberAndClientAndCvv(String numberCard, Client client, int cvv);
    List<Card> findAllByActiveAndClient(boolean active, Client client);
    void saveCard(Card card);
}
