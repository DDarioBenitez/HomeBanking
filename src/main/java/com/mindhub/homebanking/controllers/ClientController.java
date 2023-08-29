package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.utils.RandomNumberGenerator.accountNumberGenerator;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getAll() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {// en caso de que algun campo llegue vacio se develve un ResponseEntity con un mensaje y el http status
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) { //en caso de que exista un cleinte ya reguistrado con el mismo email devuelve un ResponseEntity con un mensaje y el http status
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password));//en caso de que sea estee todo bien se instancia un nuevo cliente

        String numberAccount= "VIN"+accountNumberGenerator();// Genero un numero de cuenta aleatorio

        while (accountRepository.findByNumber(numberAccount) != null){// Verifico que el numero de cuenta no estee en uso
            numberAccount= "VIN"+accountNumberGenerator();// en caso de que el numero de cuenta ya tenga dueño genero un nuevo numero de cuenta
        }

        Account account= new Account(numberAccount,LocalDate.now(),0);//si el numero de cuenta no existe en la base de datos instancio una nueva cuenta
        accountRepository.save(account);// guardo en la base de datos la cuenta instanciada
        client.addAccount(account);// le agrego la cuenta creada al cliente creado,
        clientRepository.save(client);// guardo el cliente una vez tiene agregado la cuenta

        return new ResponseEntity<>(HttpStatus.CREATED);// una vez terminado todo el proceso retorno un response entity con un http estatus en este caso created
    }

    @RequestMapping(path = "/clients/current", method = RequestMethod.GET) //mapea las peticiones tipo Get que recibe este endPoint
    public ClientDTO getCurrentClient(Authentication authentication) {//le paso como parametro el cleinte autenticado que es una instancia de la clase User, trae el username(email) y el password
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())); // busca en la base de datos el cleinte autenticado en base al email y retorna un DTO del cliente
    }
}
