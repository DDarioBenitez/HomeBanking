package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CCREDIT;
import static com.mindhub.homebanking.models.CardType.CDEBIT;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientService clientService, AccountService accountService, TransactionService transactionService, ClientLoanService clientLoanService, LoanService loanService, CardService cardService) {
		return (args) -> {
//            //Clients
//            Client melba = new Client("Melba", "Morel", "melbaM@gmail.com",passwordEncoder.encode("12345"));
//            clientService.saveClient(melba);
//
//            Client jack = new Client("Jack", "Bauer", "jackB@gmail.com",passwordEncoder.encode("12345"));
//            clientService.saveClient(jack);
//
//            Client chloe = new Client("Chloe", "O'Brian", "chloeO@gmail.com",passwordEncoder.encode("12345"));
//            clientService.saveClient(chloe);
//
//            Client kim = new Client("Kim", "Bauer", "kimB@gmail.com",passwordEncoder.encode("12345"));
//            clientService.saveClient(kim);
//
//            Client david = new Client("David", "Palmer", "davidP@gmail.com",passwordEncoder.encode("12345"));
//            clientService.saveClient(david);
//
//            Client michelle = new Client("Michelle", "Dessler", "michelleD@gmail.com",passwordEncoder.encode("12345"));
//            clientService.saveClient(michelle);
//
//            Client admin= new Client("admin","admin", "admin@gmail.com",passwordEncoder.encode("admin1"));
//            clientService.saveClient(admin);
//
//            //Accounts
//            Account vin001 = new Account("VIN001", LocalDate.now(), 5000, AccountType.SAVING);
//
//            Account vin002 = new Account("VIN002", LocalDate.now().plusDays(1), 7000,AccountType.CURRENT);
//
//            Account vin003 = new Account("VIN003", LocalDate.now().minusMonths(5), 52000,AccountType.SAVING);
//
//            Account vin004 = new Account("VIN004", LocalDate.now().minusYears(1), 523000,AccountType.CURRENT);
//
//            Account vin005 = new Account("VIN005", LocalDate.now().minusDays(12), 545000,AccountType.SAVING);
//
//            Account vin006 = new Account("VIN006", LocalDate.now().minusWeeks(1), 100,AccountType.CURRENT);
//
//            Account vin007 = new Account("VIN007", LocalDate.now().minusYears(2), 70000,AccountType.SAVING);
//
//            Account vin008 = new Account("VIN008", LocalDate.now().minusDays(25), 12000,AccountType.CURRENT);
//
//            Account vin009 = new Account("VIN009", LocalDate.now().minusWeeks(2), 85000,AccountType.SAVING);
//
//            Account vin010 = new Account("VIN010", LocalDate.now().minusMonths(2), 95000,AccountType.CURRENT);
//
//            //Transactions
//            Transaction tr1= new Transaction(DEBIT,500,"x", LocalDateTime.now().minusDays(8),12);
//
//            Transaction tr2= new Transaction(CREDIT,5200,"x", LocalDateTime.now().minusDays(6),12);
//
//            Transaction tr3= new Transaction(DEBIT,5010.23,"x", LocalDateTime.now().minusDays(5),12);
//
//            Transaction tr4= new Transaction(DEBIT,12500,"x", LocalDateTime.now().minusHours(17),12);
//
//            Transaction tr5= new Transaction(CREDIT,700,"x", LocalDateTime.now().minusHours(18),12);
//
//            Transaction tr6= new Transaction(CREDIT,12200,"x", LocalDateTime.now().minusHours(1),12);
//
//            Transaction tr7= new Transaction(DEBIT,9800.92,"x", LocalDateTime.now(),12);
//
//            Transaction tr8= new Transaction(DEBIT,500,"x", LocalDateTime.now().minusDays(12),12);
//
//            Transaction tr9= new Transaction(CREDIT,5200,"x", LocalDateTime.now().minusDays(3),12);
//
//            Transaction tr10= new Transaction(DEBIT,5010.23,"x", LocalDateTime.now().minusDays(2),12);
//
//            Transaction tr11= new Transaction(DEBIT,12500,"x", LocalDateTime.now().minusHours(10),12);
//
//            Transaction tr12= new Transaction(CREDIT,700,"x", LocalDateTime.now().minusHours(7),12);
//
//            Transaction tr13= new Transaction(CREDIT,12200,"x", LocalDateTime.now().minusHours(5),12);
//
//            Transaction tr14= new Transaction(DEBIT,9800.92,"x", LocalDateTime.now().minusHours(2),12);
//
//            //Loans
//            Loan mortgage= new Loan("Mortgage", 500000, List.of(12,24,36,48,60),List.of(3.00,6.2,12.1,18.23,45.12));
//            loanService.saveLoan(mortgage);
//            Loan personal= new Loan("Personal", 100000, List.of(6,12,24),List.of(3.00,6.2,12.1));
//            loanService.saveLoan(personal);
//            Loan car= new Loan("Car", 300000,List.of(6,12,24,36),List.of(3.00,6.2,12.1));
//            loanService.saveLoan(car);
//
//            //ClientLoan
//
//            ClientLoan cl1=new ClientLoan(400000,60);
//            ClientLoan cl2=new ClientLoan(50000,12);
//            ClientLoan cl3=new ClientLoan(100000,24);
//            ClientLoan cl4= new ClientLoan(200000,36);
//
//           melba.addClientLoan(cl1);
//           mortgage.addClientLoan(cl1);
//           melba.addClientLoan(cl2);
//           personal.addClientLoan(cl2);
//           jack.addClientLoan(cl3);
//           personal.addClientLoan(cl3);
//           jack.addClientLoan(cl4);
//           car.addClientLoan(cl4);
//
//           clientLoanService.saveClientLoan(cl1);
//           clientLoanService.saveClientLoan(cl2);
//           clientLoanService.saveClientLoan(cl3);
//           clientLoanService.saveClientLoan(cl4);
//
//           //Cards
//            Card card1= new Card(CDEBIT, GOLD,LocalDate.now(), 331, "4000 2324 5432 1298","Melba Morel");
//            Card card2= new Card(CCREDIT,TITANIUM, LocalDate.now(), 234, "4513 9821 7623 5401","Melba Morel");
//            Card card3= new Card(CCREDIT,SILVER, LocalDate.now(),129,"4513 2098 6372 6348", "Jack Bauer");
//            melba.addCard(card1);
//            melba.addCard(card2);
//            jack.addCard(card3);
//            cardService.saveCard(card1);
//            cardService.saveCard(card2);
//            cardService.saveCard(card3);
//
//			//Assignment Accounts
//			melba.addAccount(vin001);
//			melba.addAccount(vin002);
//			jack.addAccount(vin003);
//            chloe.addAccount(vin008);
//            chloe.addAccount(vin010);
//            kim.addAccount(vin006);
//            kim.addAccount(vin009);
//            david.addAccount(vin005);
//			michelle.addAccount(vin004);
//			michelle.addAccount(vin007);
//
//            clientService.saveClient(melba);
//            accountService.saveAccount(vin001);
//            accountService.saveAccount(vin002);
//            accountService.saveAccount(vin003);
//            accountService.saveAccount(vin004);
//            accountService.saveAccount(vin005);
//            accountService.saveAccount(vin006);
//            accountService.saveAccount(vin007);
//            accountService.saveAccount(vin008);
//            accountService.saveAccount(vin009);
//            accountService.saveAccount(vin010);
//
//            vin001.addTransactions(tr1);
//            vin001.addTransactions(tr2);
//            vin001.addTransactions(tr3);
//            vin001.addTransactions(tr4);
//            vin001.addTransactions(tr5);
//            vin001.addTransactions(tr6);
//            vin002.addTransactions(tr7);
//            vin002.addTransactions(tr8);
//            vin002.addTransactions(tr9);
//            vin002.addTransactions(tr10);
//            vin002.addTransactions(tr11);
//            vin002.addTransactions(tr12);
//            vin002.addTransactions(tr13);
//            vin002.addTransactions(tr14);
//            transactionService.saveTransaction(tr1);
//            transactionService.saveTransaction(tr2);
//            transactionService.saveTransaction(tr3);
//            transactionService.saveTransaction(tr4);
//            transactionService.saveTransaction(tr5);
//            transactionService.saveTransaction(tr6);
//            transactionService.saveTransaction(tr7);
//            transactionService.saveTransaction(tr8);
//            transactionService.saveTransaction(tr9);
//            transactionService.saveTransaction(tr10);
//            transactionService.saveTransaction(tr11);
//            transactionService.saveTransaction(tr12);
//            transactionService.saveTransaction(tr13);
//            transactionService.saveTransaction(tr14);

        };
	}
}
