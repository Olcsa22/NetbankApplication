package hu.bingus.netbankapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.exceptions.EntityAlreadyExistsException;
import hu.bingus.netbankapp.exceptions.EntityNotFoundException;
import hu.bingus.netbankapp.exceptions.UnaccessibleByUserException;
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
    public ResponseEntity createAccount(@RequestBody Account account) throws EntityAlreadyExistsException {
        Boolean response = accountService.addAccount(account);
        if(response){
            return new ResponseEntity("\"reason\":\"sikeres mentés!\"",HttpStatus.OK);
        }else{
            return new ResponseEntity("\"reason\":\"sikertelen mentés!\"",HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteAccountAdmin", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity deleteAccountAdmin(@RequestParam Long id) throws EntityNotFoundException {
        Boolean response = accountService.deleteAccountByIdAdmin(id);
        if(response){
            return new ResponseEntity<>("{\"reason\":\"Sikeres törlés\"}", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("{\"reason\":\"Sikertelen törlés\"}",HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteAccountClient", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity deleteAccountClient(@RequestParam Long id, Principal principal) throws UnaccessibleByUserException, EntityNotFoundException {
        Boolean response = accountService.deleteAccountByIdUser(id, principal);
        if(response){
            return new ResponseEntity<>("{\"reason\":\"Sikeres törlés\"}", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("{\"reason\":\"Sikertelen törlés\"}",HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getBalanceClient", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getBalanceClient(@RequestParam Long id, Principal principal) throws UnaccessibleByUserException, EntityNotFoundException {
        Long response=accountService.getBalanceClient(id,principal);
        return new ResponseEntity<>("{\"balance\":"+response.toString()+"}",HttpStatus.OK);
    }

    @RequestMapping(value = "/getBalanceAdmin", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getBalanceAdmin(@RequestParam Long id) throws EntityNotFoundException {
        Long response=accountService.getBalance(id);
        return new ResponseEntity<>("{\"balance\":"+response.toString()+"}",HttpStatus.OK);
    }

}
