package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

import static com.mindhub.homebanking.models.CardType.*;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name= "native", strategy = "native")
    private long id;
    private String cardHolder;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate truDate;
    private CardType type;
    private CardColor color;
    private String number;
    private boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    public Card (){

    }
    public Card(CardType type, CardColor color, LocalDate fromDate, int cvv, String number, String cardHolder){
        this.type=type;
        this.color=color;
        this.fromDate=fromDate;
        this.cvv=cvv;
        this.number= number;
        this.truDate = this.fromDate.plusYears(5);
        this.cardHolder = cardHolder;
        this.active=true;
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return truDate;
    }

    public void setTruDate(LocalDate truDate) {
        this.truDate = truDate;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }
    public String getNumber(){
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean getActive(){
        return this.active;
    }
    public void setActive(boolean active){
        this.active=active;
    }
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
