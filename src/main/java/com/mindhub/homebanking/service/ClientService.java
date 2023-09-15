package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientForAdminDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientForAdminDTO> getAllClientsDTO();

    List<Client> getAllClients();
    Client findById(long id);

    Client findByEmail(String email);
    Client findByNumberOfCard(String numberOfCard);

    void saveClient(Client client);
}
