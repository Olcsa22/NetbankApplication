package hu.bingus.netbankapp.service;

import hu.bingus.netbankapp.exceptions.EntityAlreadyExistsException;
import hu.bingus.netbankapp.exceptions.EntityNotFoundException;
import hu.bingus.netbankapp.exceptions.UnaccessibleByUserException;
import hu.bingus.netbankapp.model.Account;

import java.security.Principal;

public interface AccountService {

    Boolean addAccount(Account account) throws EntityAlreadyExistsException;

    Boolean deleteAccountByIdAdmin(Long id) throws EntityNotFoundException;

    Boolean deleteAccountByIdUser(Long id, Principal principal) throws UnaccessibleByUserException, EntityNotFoundException;

    Long getBalance(Long id) throws EntityNotFoundException;

    public Long getBalanceClient(Long id, Principal principal) throws EntityNotFoundException, UnaccessibleByUserException;

}
