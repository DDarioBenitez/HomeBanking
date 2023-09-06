package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAllAccountsDTO() {
        return this.getAllAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account findByNumberAndClient(String number, Client client) {
        return accountRepository.findByNumberAndClient(number,client);
    }

    @Override
    public Account findByIdAndClient(long id, Client client) {
        return accountRepository.findByIdAndClient(id,client);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
