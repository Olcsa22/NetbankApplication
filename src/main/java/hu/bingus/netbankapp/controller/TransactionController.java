package hu.bingus.netbankapp.controller;


import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.exceptions.EntityNotFoundException;
import hu.bingus.netbankapp.exceptions.UnaccessibleByUserException;
import hu.bingus.netbankapp.model.Transaction;
import hu.bingus.netbankapp.service.TransactionServiceImpl;
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
@RequiredArgsConstructor
@PreAuthorize("denyAll")
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionServiceImpl transactionService;


    @RequestMapping(value = "/createTransaction", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity createTransaction(@RequestBody Transaction transaction) throws EntityNotFoundException, UnaccessibleByUserException {
        Boolean response = transactionService.createTransaction(transaction);
        if(response){
            return new ResponseEntity<>("{\"reason\":\"Sikeres tranzakció létrehozás\"}", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("{\"reason\":\"Sikertelen tranzakció létrehozás\"}",HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/stornoTransactionAdmin", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity stornoTransactionAdmin(@RequestParam Long id) throws EntityNotFoundException, UnaccessibleByUserException {
        Boolean response = transactionService.stornoTransaction(id);
        if(response){
            return new ResponseEntity<>("{\"reason\":\"Sikeres tranzakció sztornózás\"}", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("{\"reason\":\"Sikertelen tranzakció sztornózás\"}",HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/stornoTransactionClient", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity stornoTransactionClient(@RequestParam Long id, Principal principal) throws EntityNotFoundException, UnaccessibleByUserException {
        Boolean response = transactionService.stornoTransactionClient(id, principal);
        if(response){
            return new ResponseEntity<>("{\"reason\":\"Sikeres tranzakció sztornózás\"}", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("{\"reason\":\"Sikertelen tranzakció sztornózás\"}",HttpStatus.BAD_REQUEST);
        }
    }

}
