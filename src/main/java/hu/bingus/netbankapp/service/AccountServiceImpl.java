package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import hu.bingus.netbankapp.util.ContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AbstractMap.SimpleEntry<JsonNode, HttpStatus> addAccount(Account account) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("accountNumber",account.getAccountNumber());
            List<Account> testAccounts = super.getWhereEq(params);
            if(testAccounts!=null) {
                super.getCurrentSession().saveOrUpdate(account);
                JsonNode jsonNode = objectMapper.createObjectNode().put("reason", "Sikeres számla létrehozás");
                return new AbstractMap.SimpleEntry<>(jsonNode, HttpStatus.OK);
            }else{
                JsonNode jsonNode = objectMapper.createObjectNode().put("reason", "Már létezik ilyen számlaszámmal számla");
                return new AbstractMap.SimpleEntry<>(jsonNode, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            log.error("AccountServiceImpl - addAccount: "+e);
            JsonNode jsonNode= objectMapper.createObjectNode().put("reason","Hiba a számla létrehozása közben");
            return new AbstractMap.SimpleEntry<>(jsonNode,HttpStatus.BAD_REQUEST);
        }
    }

    public AbstractMap.SimpleEntry<JsonNode, HttpStatus> deleteAccountByIdAdmin(Long id){
        Account account1 = super.findById(id);
        if(account1!=null){
            super.getCurrentSession().delete(account1);
            JsonNode jsonNode= objectMapper.createObjectNode().put("reason","Sikeres törlés");
            return new AbstractMap.SimpleEntry<>(jsonNode,HttpStatus.OK);
        }else{
            JsonNode jsonNode= objectMapper.createObjectNode().put("reason","Sikertelen törlés, nincs ilyen id-jú account");
            return new AbstractMap.SimpleEntry<>(jsonNode,HttpStatus.BAD_REQUEST);
        }

    }

    public AbstractMap.SimpleEntry<JsonNode, HttpStatus> deleteAccountByIdUser(Long id, Principal principal){
        User user = ContextProvider.getBean(UserServiceImpl.class).findByUsername(principal.getName());
        Account account1 = super.findById(id);
        if(account1!=null){
            if(user.getId() == account1.getUserId()) {
                super.getCurrentSession().delete(account1);
                JsonNode jsonNode = objectMapper.createObjectNode().put("reason", "Sikeres törlés");
                return new AbstractMap.SimpleEntry<>(jsonNode, HttpStatus.OK);
            }else{
                JsonNode jsonNode = objectMapper.createObjectNode().put("reason", "Sikertelen törlés");
                return new AbstractMap.SimpleEntry<>(jsonNode, HttpStatus.BAD_REQUEST);
            }
        }else{
            JsonNode jsonNode= objectMapper.createObjectNode().put("reason","Sikertelen törlés, nincs ilyen id-jú account");
            return new AbstractMap.SimpleEntry<>(jsonNode,HttpStatus.BAD_REQUEST);
        }

    }

    public AbstractMap.SimpleEntry<JsonNode,HttpStatus> getBalance(Long id){
        Account account = findById(id);
        if(account!=null){
            JsonNode node = objectMapper.createObjectNode().put("reason","Az egyenleg: "+account.getBalance().toString());
            return new AbstractMap.SimpleEntry<>(node,HttpStatus.OK);
        }else{
            JsonNode node = objectMapper.createObjectNode().put("reason","Nem található számla");
            return new AbstractMap.SimpleEntry<>(node,HttpStatus.OK);
        }
    }

    public AbstractMap.SimpleEntry<JsonNode,HttpStatus> getBalanceClient(Long id, Principal principal){
        Account account = findById(id);
        if(account!=null){
            User user = ContextProvider.getBean(UserServiceImpl.class).findByUsername(principal.getName());
            if(account.getUserId()== user.getId()){
                return getBalance(id);
            }else{
                JsonNode node = objectMapper.createObjectNode().put("reason","Nem a felhasználóhoz tartozó számla");
                return new AbstractMap.SimpleEntry<>(node,HttpStatus.BAD_REQUEST);
            }
        }else{
            JsonNode node = objectMapper.createObjectNode().put("reason","Nem található számla");
            return new AbstractMap.SimpleEntry<>(node,HttpStatus.OK);
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
