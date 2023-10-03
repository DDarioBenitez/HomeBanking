package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Assertions;



import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

//    private List<Loan> loans;
//    @BeforeEach
//    public void setUpLoans(){
//        loans = loanRepository.findAll();
//    }
//    @Test
//    public void existLoans(){
//        assertThat(loans,is(not(empty())));
//    }
//
//    @Test
//    public void existPersonalLoan(){
//        assertThat(loans, hasItem(hasProperty("name", is("Personal Loan"))));
//    }
//
//    @Test
//    public  void getClientWhitEmail(){
//        Client client=clientRepository.findByEmail("davidP@gmail.com");
//        assertThat(client, hasProperty("email", is("davidP@gmail.com")));
//    }
//    @Test
//    public void getClients(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients,is(not(empty())));
//    }
//    @Test
//    public void getAccounts(){
//        List<Account> accounts= accountRepository.findAll();
//        assertThat(accounts, is(not(empty())));
//    }
//    @Test
//    public void getAccountWhitOutNumber(){
//        Account account=  accountRepository.findByNumber("");
//        Assertions.assertNull(account, "The number account is empty or not find in the DB");
//    }
//    @Test
//    public void getCards(){
//        List<Card> cards= cardRepository.findAll();
//        assertThat(cards,is(not(empty())));
//    }
//    @Test
//    public void getCardWhitNumber(){
//        Card card= cardRepository.findByNumber("4000 2324 5432 1298");
//        Assertions.assertNotNull(card,"Find success");
//    }
//    @Test
//    public void getTransactions(){
//        List<Transaction> transactions= transactionRepository.findAll();
//        assertThat(transactions,is(not(empty())));
//    }
//    @Test
//    public void getTransaction(){
//        Transaction transaction= transactionRepository.findById(28L).orElseThrow();
//        Assertions.assertNotNull(transaction, "Funciono");
//    }
}
