package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientForAdminDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientForAdminDTO> getAllClientsDTO() {
       return this.getAllClients().stream().map(ClientForAdminDTO::new).collect(toList());
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client findByNumberOfCard(String numberOfCard) {
        return clientRepository.findByNumberOfCard(numberOfCard);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
