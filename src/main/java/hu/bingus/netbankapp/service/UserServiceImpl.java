package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.exceptions.EntityAlreadyExistsException;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import hu.bingus.netbankapp.util.ContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("UserService")
@Slf4j
@Transactional
public class UserServiceImpl extends AbstractCriteriaService<User> implements UserService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserServiceImpl() {
        super(User.class);
    }

    @Override
    public Boolean register(UserDTO user) throws EntityAlreadyExistsException {
        Map<String, Object> params = new HashMap<>();
        params.put("username",user.getUsername());
        List<User> usersWithUsername = getWhereEq(params);

        if(usersWithUsername!=null){
            throw new EntityAlreadyExistsException("Már létezik ezzel a névvel felhasználó!");
        }else if(!user.getPassword().equals(user.getPasswordAgain())){
            throw new BadCredentialsException("A jelszavak nem egyeznek!");
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
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
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
