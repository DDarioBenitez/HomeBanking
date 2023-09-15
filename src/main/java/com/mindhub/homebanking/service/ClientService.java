package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientForAdminDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientService {

    List<ClientForAdminDTO> getAllClientsDTO();

    List<Client> getAllClients();
    Client findById(long id);

    Client findByEmail(String email);
    Client findByCardNumber(String cardNumber);

    void saveClient(Client client);
}
