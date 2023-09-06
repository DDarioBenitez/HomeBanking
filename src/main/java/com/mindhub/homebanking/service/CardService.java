package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

public interface CardService {
    Card findByTypeAndColorAndClient(CardType type, CardColor color, Client client);
    Card findByNumber(String number);
    void saveCard(Card card);
}
