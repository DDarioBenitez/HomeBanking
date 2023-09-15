package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientForAdminDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
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
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientForAdminDTO> getAll() {
        return clientService.getAllClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        Client client=clientService.findById(id);
        return new ClientDTO(client);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {// en caso de que algun campo llegue vacio se develve un ResponseEntity con un mensaje y el http status
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email)!= null) { //en caso de que exista un cleinte ya reguistrado con el mismo email devuelve un ResponseEntity con un mensaje y el http status
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password));//en caso de que sea estee todo bien se instancia un nuevo cliente

        String numberAccount= "VIN"+accountNumberGenerator();// Genero un numero de cuenta aleatorio

        while (accountService.findByNumber(numberAccount) != null){// Verifico que el numero de cuenta no estee en uso
            numberAccount= "VIN"+accountNumberGenerator();// en caso de que el numero de cuenta ya tenga dueño genero un nuevo numero de cuenta
        }

        Account account= new Account(numberAccount,LocalDate.now(),0, AccountType.SAVING);//si el numero de cuenta no existe en la base de datos instancio una nueva cuenta
        accountService.saveAccount(account);// guardo en la base de datos la cuenta instanciada
        client.addAccount(account);// le agrego la cuenta creada al cliente creado,
        clientService.saveClient(client);// guardo el cliente una vez tiene agregado la cuenta

        return new ResponseEntity<>(HttpStatus.CREATED);// una vez terminado todo el proceso retorno un response entity con un http estatus en este caso created
    }

    @GetMapping("/clients/current") //mapea las peticiones tipo Get que recibe este endPoint
    public ClientDTO getCurrentClient(Authentication authentication) {//le paso como parametro el cleinte autenticado que es una instancia de la clase User, trae el username(email) y el password
        return new ClientDTO(clientService.findByEmail(authentication.getName())); // busca en la base de datos el cleinte autenticado en base al email y retorna un DTO del cliente
    }
}
