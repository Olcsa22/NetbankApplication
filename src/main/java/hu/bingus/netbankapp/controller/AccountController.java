package hu.bingus.netbankapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.service.AccountService;
import hu.bingus.netbankapp.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.AbstractMap;

@Controller
@PreAuthorize("denyAll")
@RequiredArgsConstructor
@RequestMapping(value = "/account")
public class AccountController {


    private final AccountServiceImpl accountService;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createAccount(@RequestBody Account account){
        AbstractMap.SimpleEntry<JsonNode, HttpStatus> response = accountService.addAccount(account);
        return new ResponseEntity(response.getValue(),response.getValue());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/deleteAccount", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity deleteAccount(@RequestBody Long id){
        AbstractMap.SimpleEntry<JsonNode, HttpStatus> response = accountService.deleteAccountById(id);
        return new ResponseEntity(response.getValue(),response.getValue());
    }

}
