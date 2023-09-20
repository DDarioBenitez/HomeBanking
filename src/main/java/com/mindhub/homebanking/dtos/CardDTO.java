package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardHolder;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate truDate;
    private CardType type;
    private CardColor color;
    private String number;
    private boolean active;

    public CardDTO(Card card){
        this.id=card.getId();
        this.cardHolder= card.getCardHolder();
        this.number=card.getNumber();
        this.cvv= card.getCvv();
        this.fromDate=card.getFromDate();
        this.truDate=card.getThruDate();
        this.type=card.getType();
        this.color=card.getColor();
        this.active=card.getActive();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getTruDate() {
        return truDate;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public boolean isActive() {
        return active;
    }
}
