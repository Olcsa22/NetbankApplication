package hu.bingus.netbankapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.AbstractMap;

@Controller
@PreAuthorize("denyAll")
@RequiredArgsConstructor
@RequestMapping(value = "/account")
public class AccountController {


    private final AccountServiceImpl accountService;


    @RequestMapping(value = "/createAccount", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createAccount(@RequestBody Account account){
        AbstractMap.SimpleEntry<JsonNode, HttpStatus> response = accountService.addAccount(account);
        return new ResponseEntity(response.getValue(),response.getValue());
    }

    @RequestMapping(value = "/deleteAccountAdmin", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity deleteAccountAdmin(@RequestParam Long id){
        AbstractMap.SimpleEntry<JsonNode, HttpStatus> response = accountService.deleteAccountByIdAdmin(id);
        return new ResponseEntity(response.getValue(),response.getValue());
    }

    @RequestMapping(value = "/deleteAccountClient", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity deleteAccountAdmin(@RequestParam Long id, Principal principal){
        AbstractMap.SimpleEntry<JsonNode, HttpStatus> response = accountService.deleteAccountByIdUser(id, principal);
        return new ResponseEntity(response.getValue(),response.getValue());
    }

    @RequestMapping(value = "/getBalanceClient", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getBalanceClient(@RequestParam Long id, Principal principal){
        AbstractMap.SimpleEntry<JsonNode,HttpStatus> response=accountService.getBalanceClient(id,principal);
        return new ResponseEntity<>(response.getKey(),response.getValue());
    }

    @RequestMapping(value = "/getBalanceAdmin", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getBalanceAdmin(@RequestParam Long id){
        AbstractMap.SimpleEntry<JsonNode,HttpStatus> response=accountService.getBalance(id);
        return new ResponseEntity<>(response.getKey(),response.getValue());
    }

}
