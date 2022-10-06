package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.model.Account;
import org.springframework.http.HttpStatus;

import java.util.AbstractMap;

public interface AccountService {

    AbstractMap.SimpleEntry<JsonNode, HttpStatus> addAccount(Account account);

    AbstractMap.SimpleEntry<JsonNode, HttpStatus> deleteAccountById(Long id);

}
