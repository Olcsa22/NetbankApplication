package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import hu.bingus.netbankapp.util.ContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("UserService")
@Slf4j
public class UserServiceImpl extends AbstractCriteriaService<User> implements UserService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserServiceImpl() {
        super(User.class);
    }

    @Override
    public AbstractMap.SimpleEntry register(UserDTO user) {
        Map<String, Object> params = new HashMap<>();
        params.put("username",user.getUsername());
        List<User> usersWithUsername = getWhereEq(params);

        if(usersWithUsername!=null){
            JsonNode json = objectMapper.createObjectNode().put("reason","Ezzel a felhasználónévvel már létezik felhasználó");
            AbstractMap.SimpleEntry<JsonNode,HttpStatus> entry = new AbstractMap.SimpleEntry<>(json,HttpStatus.BAD_REQUEST);

            return entry;
        }else if(!user.getPassword().equals(user.getPasswordAgain())){
            JsonNode json = objectMapper.createObjectNode().put("reason","A jelszavak nem egyeznek");
            AbstractMap.SimpleEntry<JsonNode,HttpStatus> entry = new AbstractMap.SimpleEntry<>(json,HttpStatus.BAD_REQUEST);

            return entry;
        }else{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User userToSave = new User();
            userToSave.setUsername(user.getUsername());
            userToSave.setPassword(encoder.encode(user.getPassword()));
            try {
                super.getCurrentSession().save(userToSave);

                Account account;
                String accountNumber;
                do{
                    accountNumber = generateAccountNumber();
                    account = ContextProvider.getBean(AccountServiceImpl.class).findByAccountNumber(accountNumber);
                }while(account!=null);
                account = new Account();
                account.setUserId(userToSave.getId());
                account.setAccountNumber(accountNumber);
                account.setBalance(0L);
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().saveOrUpdate(account);

            }catch (Exception e){
                log.error("HomeServiceImpl - register: "+e);
                JsonNode json = objectMapper.createObjectNode().put("reason","A hiba a regisztráció közben!");
                AbstractMap.SimpleEntry<JsonNode,HttpStatus> entry = new AbstractMap.SimpleEntry<>(json,HttpStatus.INTERNAL_SERVER_ERROR);
                return entry;
            }
            JsonNode json = objectMapper.createObjectNode().put("reason","Sikeres regisztráció!");
            AbstractMap.SimpleEntry<JsonNode,HttpStatus> entry = new AbstractMap.SimpleEntry<>(json,HttpStatus.OK);
            return entry;
        }
    }

    @Override
    public User findByUsername(String username) {
        Map<String,Object> params = new HashMap<>();
        params.put("username",username);
        List<User> users = super.getWhereEq(params);
        if(users!=null){
            return users.get(0);
        }else{
            return null;
        }
    }

    private String generateAccountNumber(){
        String start = "BE";
        Random value = new Random();

        int r1 = value.nextInt(10);
        int r2 = value.nextInt(10);
        start += Integer.toString(r1) + Integer.toString(r2) + " ";

        int count = 0;
        int n = 0;
        for(int i =0; i < 12;i++)
        {
            if(count == 4)
            {
                start += " ";
                count =0;
            }
            else
                n = value.nextInt(10);
            start += Integer.toString(n);
            count++;

        }
        return start;
    }
}
