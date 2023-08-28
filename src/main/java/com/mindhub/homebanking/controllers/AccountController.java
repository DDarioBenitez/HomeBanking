package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.RandomNumberGenerator.accountNumberGenerator;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAll(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @PostMapping("/api/clients/current/accounts") //servlet que permite unicamente peticiones del tipo post a este endpoint
    public ResponseEntity<Object> createdAccount(Authentication authentication){ //controla la creacion de cuentas nuevas

        if (authentication !=null){ //verifica si existe un cliente autenticado y si existe entra al if

            Client client = clientRepository.findByEmail(authentication.getName()); // busco el cliente autenticado en la base de datos y lo guardo en una variable para trabajar con el

            if(client.getAccounts().toArray().length < 3){ //confirmo que el cliente tenga menos de 3 cuentas

                String accountNewNumber="VIN"+ accountNumberGenerator(); // guardo el numbero de cuenta en una variable concatenandole el VIN que viene por defecto para todas las cuentas

                while(accountRepository.findByNumber(accountNewNumber)!=null){ //verficio que no exista en la base de datos ninguna cuenta con el mismo numero de cuenta creado
                    accountNewNumber="VIN"+accountNumberGenerator(); // en caso de que si exista creo un numero de cuenta nuevo
                }

                Account newAccount = new Account(accountNewNumber,LocalDate.now(),0); // creo la nueva cuenta

                client.addAccount(newAccount); // añado la nueva cuenta en el cliente autenticado

                accountRepository.save(newAccount); // guardo la nueva cuenta


                clientRepository.save(client); //guardo nuevamente el cliente con la cuenta añadida

                return new ResponseEntity<>("Account Created", HttpStatus.CREATED);

            }else { //Si tiene las de 3 cuentas

             return new ResponseEntity<>("Numero de cuentas Maximo alcanzado", HttpStatus.FORBIDDEN);

            }
        }else { // si no existe un cliente autenticado entra al else

            return new ResponseEntity<>("Cleinte no verificado", HttpStatus.FORBIDDEN);

        }
    }

}
