package com.mindhub.homebanking.repositories;

import java.util.List;
import java.util.Optional;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
    @Query("SELECT c FROM Client c JOIN c.cards card WHERE card.number = :number")
    Client findByCardNumber(@Param("number") String cardNumber);
}


