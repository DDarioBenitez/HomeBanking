package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native",strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    @OneToMany (mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards= new HashSet<>();

    public Client(String firstName, String lastName, String email,String password){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.password=password;
    }

   public Client(){

   }
    public String getFirstName(){

        return this.firstName;
    }

    public String getLastName(){

        return this.lastName;
    }

    public String getEmail(){

        return this.email;
    }

    public long getId(){

        return this.id;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<Card> getCards(){
        return  cards;
    }
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }
    public String toString() {
        return firstName + " " + lastName+ " "+email;
    }
}
