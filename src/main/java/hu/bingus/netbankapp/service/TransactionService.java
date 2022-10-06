package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.model.Transaction;
import org.springframework.http.HttpStatus;

import java.util.AbstractMap;

public interface TransactionService {
    AbstractMap.SimpleEntry<JsonNode, HttpStatus> createTransaction(Transaction transaction);

    AbstractMap.SimpleEntry<JsonNode, HttpStatus> stornoTransaction(Long id);

}
