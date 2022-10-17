package hu.bingus.netbankapp.service;

import hu.bingus.netbankapp.exceptions.EntityNotFoundException;
import hu.bingus.netbankapp.exceptions.UnaccessibleByUserException;
import hu.bingus.netbankapp.model.Transaction;

import java.security.Principal;

public interface TransactionService {
    Boolean createTransaction(Transaction transaction) throws EntityNotFoundException, UnaccessibleByUserException;

    Boolean stornoTransaction(Long id) throws EntityNotFoundException, UnaccessibleByUserException;

    public Boolean stornoTransactionClient(Long id, Principal principal) throws EntityNotFoundException, UnaccessibleByUserException;

}
