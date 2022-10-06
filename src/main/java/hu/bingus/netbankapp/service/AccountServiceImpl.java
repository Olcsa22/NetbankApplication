package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("AccountService")
@Transactional
public class AccountServiceImpl extends AbstractCriteriaService<Account> implements AccountService {

    private final ObjectMapper objectMapper;

    public AccountServiceImpl(ObjectMapper objectMapper) {
        super(Account.class);
        this.objectMapper=objectMapper;
    }

    @Override
    public AbstractMap.SimpleEntry<JsonNode, HttpStatus> addAccount(Account account) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("accountNumber",account.getAccountNumber());
            Account testAccount = super.getWhereEq(params).get(0);
            if(testAccount!=null) {

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

    public AbstractMap.SimpleEntry<JsonNode, HttpStatus> deleteAccountById(Long id){
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
}
