package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.exceptions.EntityAlreadyExistsException;
import hu.bingus.netbankapp.exceptions.EntityNotFoundException;
import hu.bingus.netbankapp.exceptions.UnaccessibleByUserException;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import hu.bingus.netbankapp.util.ContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.security.Principal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("AccountService")
@Transactional
public class AccountServiceImpl extends AbstractCriteriaService<Account> implements AccountService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AccountServiceImpl() {
        super(Account.class);
    }

    @Override
    public Boolean addAccount(Account account) throws EntityAlreadyExistsException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("accountNumber",account.getAccountNumber());
            List<Account> testAccounts = super.getWhereEq(params);
            if(testAccounts==null) {
                return Boolean.TRUE;
            }else{
              throw new EntityAlreadyExistsException("Már létezik ilyen bankszámla");
            }
        }catch (RuntimeException e){
            log.error("AccountServiceImpl - addAccount: "+e);
            return Boolean.FALSE;
        }
    }

    public Boolean deleteAccountByIdAdmin(Long id) throws EntityNotFoundException {
        Account account1 = super.findById(id);
        if(account1!=null){
            super.getCurrentSession().delete(account1);
            return Boolean.TRUE;
        }else{
            throw new EntityNotFoundException("Nem létezik ilyen ID-jú entitás");
        }

    }

    public Boolean deleteAccountByIdUser(Long id, Principal principal) throws UnaccessibleByUserException, EntityNotFoundException {
        User user = ContextProvider.getBean(UserServiceImpl.class).findByUsername(principal.getName());
        Account account1 = super.findById(id);
        if(account1!=null){
            if(user.getId() == account1.getUserId()) {
                super.getCurrentSession().delete(account1);
                return Boolean.TRUE;
            }else{
                throw new UnaccessibleByUserException("Nem a felhasználóhoz tartozó bankszámla.");
            }
        }else{
            throw new EntityNotFoundException("Nem található ilyen bankszámla.");
        }

    }

    public Long getBalance(Long id) throws EntityNotFoundException {
        try {
            Account account = findById(id);
            if (account != null) {
                return account.getBalance();
            } else {
                throw new EntityNotFoundException("Számla nem található");
            }
        }catch (NoResultException exception){
            throw new EntityNotFoundException("Számla nem található", exception);
        }
    }

    public Long getBalanceClient(Long id, Principal principal) throws EntityNotFoundException, UnaccessibleByUserException {
        Account account = findById(id);
        if(account!=null){
            User user = ContextProvider.getBean(UserServiceImpl.class).findByUsername(principal.getName());
            if(account.getUserId()== user.getId()){
                return getBalance(id);
            }else{
                throw new UnaccessibleByUserException("Nem a felhasználóhoz tartozó számla");
            }
        }else{
            throw new EntityNotFoundException("Nem található számla");
        }

    }

    public Account findByAccountNumber(String accountNumber){
        HashMap<String,Object> map = new HashMap<>();
        map.put("accountNumber",accountNumber);
        List<Account> accounts = super.getWhereEq(map);
        if(accounts!=null){
            return accounts.get(0);
        }else{
            return null;
        }
    }
}
