package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAllAccountsDTO();
    List<Account> getAllAccounts();
    Account findByNumber(String number);
    Account findByNumberAndClient(String number, Client client);
    Account findByIdAndClient(long id, Client client);
    void saveAccount(Account account);
}
