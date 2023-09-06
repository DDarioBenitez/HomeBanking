package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getAllClientsDTO();

    List<Client> getAllClients();
    Client findById(long id);

    Client findByEmail(String email);

    void saveClient(Client client);
}
