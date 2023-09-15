package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CreateLoanController {
    @Autowired
    private LoanService loanService;

    @Transactional
    @PostMapping("/admin/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanDTO loanDTO , Authentication authentication){
//        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))){
//            return new ResponseEntity<>("No tiene autorizacion para esta accion", HttpStatus.FORBIDDEN);
//        }
        if (loanDTO.getName().isBlank()){
            return new ResponseEntity<>("El nombre esta en blanco", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount()<=0){
            return new ResponseEntity<>("El monto no puede ser 0 o menos que 0", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPercentage().isEmpty() || loanDTO.getPercentage().stream().anyMatch(element->element==0)){
            return new ResponseEntity<>("El porcentaje no puede ser igual o menos que 0", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayment().isEmpty() || loanDTO.getPayment().stream().anyMatch(element->element==0)){
            return new ResponseEntity<>("La propiedad cuotas no puede estar en blanco", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayment().size()<loanDTO.getPercentage().size() || loanDTO.getPayment().size()>loanDTO.getPercentage().size()){
            return new ResponseEntity<>("Las cuotas y los porcentajes de interes no coinciden", HttpStatus.FORBIDDEN);
        }
        if (loanService.findByName(loanDTO.getName())!=null){
            return new ResponseEntity<>("Ya existe un loan con el mismo nombre", HttpStatus.FORBIDDEN);
        }
        Loan loan=new Loan(loanDTO.getName(),loanDTO.getMaxAmount(),loanDTO.getPayment(),loanDTO.getPercentage());
        loanService.saveLoan(loan);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }
}
