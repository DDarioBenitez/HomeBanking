package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.implement.AccountServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.RandomNumberGenerator.accountNumberGenerator;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAll(){
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/api/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable long id, Authentication authentication){
        if (authentication==null){
            return new ResponseEntity<>("Sesion expirada", HttpStatus.FORBIDDEN);
        }
        Client client= clientService.findByEmail(authentication.getName());
        Account account= accountService.findByIdAndClientAndActive(id,client,true);
        if (account==null){
            return new ResponseEntity<>("cuenta no encontrada", HttpStatus.FORBIDDEN);
        }
        AccountDTO accountDTO= new AccountDTO(account);
        return new ResponseEntity<>(accountDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/clients/current/accounts") //servlet que permite unicamente peticiones del tipo post a este endpoint
    public ResponseEntity<Object> createdAccount(@RequestParam String type, Authentication authentication){ //controla la creacion de cuentas nuevas
        if (authentication !=null){ //verifica si existe un cliente autenticado y si existe entra al if

            Client client = clientService.findByEmail(authentication.getName()); // busco el cliente autenticado en la base de datos y lo guardo en una variable para trabajar con el

            if (type.isBlank()){
                return new ResponseEntity<>("Type no puede estar en blanco", HttpStatus.FORBIDDEN);
            }

            AccountType typeAccount;
            try { //Verifico que el type coincida con algun valor del ENUM
                typeAccount = AccountType.valueOf(type);
            } catch (IllegalArgumentException ex) { //En caso de que no coincida me dara una excepcion y le asigno el valor null a typeAccount
                typeAccount = null;
            }
            if (typeAccount==null){ //si typecoount es null devuelvo un status con un mensaje de error
                return new ResponseEntity<>("El tipo seleccionado no existe", HttpStatus.FORBIDDEN);
            }
            Set<Account> accountSet= client.getAccounts().stream().filter(Account::isActive).collect(Collectors.toSet());
            if(client.getAccounts().size()<3){ //confirmo que el cliente tenga menos de 3 cuentas
                String accountNewNumber;
                do{
                    accountNewNumber="VIN"+accountNumberGenerator();
                }
                while(accountService.findByNumber(accountNewNumber)!=null);//verficio que no exista en la base de datos ninguna cuenta con el mismo numero de cuenta creado
                     // en caso de que si exista creo un numero de cuenta nuevo


                Account newAccount = new Account(accountNewNumber,LocalDate.now(),0,typeAccount); // creo la nueva cuenta

                client.addAccount(newAccount); // añado la nueva cuenta en el cliente autenticado

                accountService.saveAccount(newAccount); // guardo la nueva cuenta


                clientService.saveClient(client); //guardo nuevamente el cliente con la cuenta añadida

                return new ResponseEntity<>("Account Created", HttpStatus.CREATED);

            }else { //Si tiene las de 3 cuentas

             return new ResponseEntity<>("Maximum number of accounts reached", HttpStatus.FORBIDDEN);

            }
        }else { // si no existe un cliente autenticado entra al else

            return new ResponseEntity<>("Unverified customer", HttpStatus.FORBIDDEN);

        }
    }
    @PatchMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(@RequestParam String numberAccount, Boolean active, Authentication authentication){
        if(numberAccount.isBlank()){
            return new ResponseEntity<>("Numero de cuenta vacio", HttpStatus.FORBIDDEN);
        }
        if (active == null || active){
            return new ResponseEntity<>("Parametro con valor incorreceto", HttpStatus.FORBIDDEN);
        }
        Client client= clientService.findByEmail(authentication.getName());
        Account account= accountService.findByNumberAndClient(numberAccount,client);
        if (account==null){
            return new ResponseEntity<>("La cuenta no pertenece al cliente", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance()>0){
            return new ResponseEntity<>("La cuenta no puede ser eliminada si el balance es diferente a 0", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().size()<=1){
            return new ResponseEntity<>("Si solo existe una cuenta no puede ser eliminada", HttpStatus.FORBIDDEN);
        }
        account.setActive(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
    }

}
