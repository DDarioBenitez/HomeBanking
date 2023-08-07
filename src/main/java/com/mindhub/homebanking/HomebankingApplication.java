package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,AccountRepository accountRepository) {
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


        };
	}
}
