package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;

    public ClientDTO (Client client){
        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        //Para usar las cuentas primero llamo las cuentas de client y las paso a stream(.stream) el cual es un objeto iterable
        // al cual se le pueden aplicar metedos tales como .map .filter, es parecido a un array de JS, una vez echo eso se hace un map
        // y le digo al map que tome cada cuenta y creee una AccountDTO y como sigue siendo un archivo stream y mi propiedad accaounts me pide
        // una collecion tipo Set uso el collects .toset() para que sea del tipo que me pide Account por que java es un lenguaje fuertemente tipado.
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

}
