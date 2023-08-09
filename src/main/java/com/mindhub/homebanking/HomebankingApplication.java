package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
            //Clients
            Client melba = new Client("Melba", "Morel", "melbaM@gmail.com");
            clientRepository.save(melba);

            Client jack = new Client("Jack", "Bauer", "jackB@gmail.com");
            clientRepository.save(jack);

            Client chloe = new Client("Chloe", "O'Brian", "chloeO@gmail.com");
            clientRepository.save(chloe);

            Client kim = new Client("Kim", "Bauer", "kimB@gmail.com");
            clientRepository.save(kim);

            Client david = new Client("David", "Palmer", "davidP@gmail.com");
            clientRepository.save(david);

            Client michelle = new Client("Michelle", "Dessler", "michelleD@gmail.com");
            clientRepository.save(michelle);


            //Accounts
            Account vin001 = new Account("VIN001", LocalDate.now(), 5000);

            Account vin002 = new Account("VIN002", LocalDate.now().plusDays(1), 7000);

            Account vin003 = new Account("VIN003", LocalDate.now().minusMonths(5), 52000);

            Account vin004 = new Account("VIN004", LocalDate.now().minusYears(1), 523000);

            Account vin005 = new Account("VIN005", LocalDate.now().minusDays(12), 545000);

            Account vin006 = new Account("VIN006", LocalDate.now().minusWeeks(1), 100);

            Account vin007 = new Account("VIN007", LocalDate.now().minusYears(2), 70000);

            Account vin008 = new Account("VIN008", LocalDate.now().minusDays(25), 12000);

            Account vin009 = new Account("VIN009", LocalDate.now().minusWeeks(2), 85000);

            Account vin010 = new Account("VIN010", LocalDate.now().minusMonths(2), 95000);

            //Transactions
            Transaction tr1= new Transaction(DEBIT,500,"x", LocalDateTime.now().minusDays(8));

            Transaction tr2= new Transaction(CREDIT,5200,"x", LocalDateTime.now().minusDays(6));

            Transaction tr3= new Transaction(DEBIT,5010.23,"x", LocalDateTime.now().minusDays(5));

            Transaction tr4= new Transaction(DEBIT,12500,"x", LocalDateTime.now().minusHours(17));

            Transaction tr5= new Transaction(CREDIT,700,"x", LocalDateTime.now().minusHours(18));

            Transaction tr6= new Transaction(CREDIT,12200,"x", LocalDateTime.now().minusHours(1));

            Transaction tr7= new Transaction(DEBIT,9800.92,"x", LocalDateTime.now());

            Transaction tr8= new Transaction(DEBIT,500,"x", LocalDateTime.now().minusDays(12));

            Transaction tr9= new Transaction(CREDIT,5200,"x", LocalDateTime.now().minusDays(3));

            Transaction tr10= new Transaction(DEBIT,5010.23,"x", LocalDateTime.now().minusDays(2));

            Transaction tr11= new Transaction(DEBIT,12500,"x", LocalDateTime.now().minusHours(10));

            Transaction tr12= new Transaction(CREDIT,700,"x", LocalDateTime.now().minusHours(7));

            Transaction tr13= new Transaction(CREDIT,12200,"x", LocalDateTime.now().minusHours(5));

            Transaction tr14= new Transaction(DEBIT,9800.92,"x", LocalDateTime.now().minusHours(2));




			//Assignment Accounts
			melba.addAccount(vin001);
			melba.addAccount(vin002);
			jack.addAccount(vin003);
            chloe.addAccount(vin008);
            chloe.addAccount(vin010);
            kim.addAccount(vin006);
            kim.addAccount(vin009);
            david.addAccount(vin005);
			michelle.addAccount(vin004);
			michelle.addAccount(vin007);

            clientRepository.save(melba);
            accountRepository.save(vin001);
            accountRepository.save(vin002);
            accountRepository.save(vin003);
            accountRepository.save(vin004);
            accountRepository.save(vin005);
            accountRepository.save(vin006);
            accountRepository.save(vin007);
            accountRepository.save(vin008);
            accountRepository.save(vin009);
            accountRepository.save(vin010);

            vin001.setTransactions(tr1);
            vin001.setTransactions(tr2);
            vin001.setTransactions(tr3);
            vin001.setTransactions(tr4);
            vin001.setTransactions(tr5);
            vin001.setTransactions(tr6);
            vin002.setTransactions(tr7);
            vin002.setTransactions(tr8);
            vin002.setTransactions(tr9);
            vin002.setTransactions(tr10);
            vin002.setTransactions(tr11);
            vin002.setTransactions(tr12);
            vin002.setTransactions(tr13);
            vin002.setTransactions(tr14);
            transactionRepository.save(tr1);
            transactionRepository.save(tr2);
            transactionRepository.save(tr3);
            transactionRepository.save(tr4);
            transactionRepository.save(tr5);
            transactionRepository.save(tr6);
            transactionRepository.save(tr7);
            transactionRepository.save(tr8);
            transactionRepository.save(tr9);
            transactionRepository.save(tr10);
            transactionRepository.save(tr11);
            transactionRepository.save(tr12);
            transactionRepository.save(tr13);
            transactionRepository.save(tr14);

        };
	}
}
