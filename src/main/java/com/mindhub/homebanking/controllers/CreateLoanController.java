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
            return new ResponseEntity<>("The name is blank", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount()<=0){
            return new ResponseEntity<>("The amount cannot be 0 or less than 0", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPercentage().isEmpty() || loanDTO.getPercentage().stream().anyMatch(element->element==0)){
            return new ResponseEntity<>("The percentage cannot be equal to or less than 0", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayment().isEmpty() || loanDTO.getPayment().stream().anyMatch(element->element==0)){
            return new ResponseEntity<>("The quotas property cannot be blank", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayment().size()<loanDTO.getPercentage().size() || loanDTO.getPayment().size()>loanDTO.getPercentage().size()){
            return new ResponseEntity<>("The installments and interest percentages do not coincide", HttpStatus.FORBIDDEN);
        }
        if (loanService.findByName(loanDTO.getName())!=null){
            return new ResponseEntity<>("There is already a loan with the same name", HttpStatus.FORBIDDEN);
        }
        Loan loan=new Loan(loanDTO.getName(),loanDTO.getMaxAmount(),loanDTO.getPayment(),loanDTO.getPercentage());
        loanService.saveLoan(loan);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }
}
