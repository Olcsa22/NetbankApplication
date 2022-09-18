package hu.bingus.netbankapp.controller;

import hu.bingus.netbankapp.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller(value = "/account")
@PreAuthorize("denyAll()")
public class AccountController {

    @PreAuthorize("hasRole(ROLE_ADMIN)")
    public ResponseEntity createAccount(Account account){

    }

}
