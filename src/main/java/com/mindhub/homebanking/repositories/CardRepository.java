package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long>{
    Card findByNumber(String number);
    Card findByTypeAndColorAndClient(CardType type, CardColor color, Client client);
    Card findByNumberAndClient(String numberCard, Client client);
    Card findByNumberAndClientAndCvv(String numberCard, Client client, int cvv);

    List<Card> findAllByActiveAndClient(boolean active, Client client);
}
