package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UserService")
@Slf4j
public class UserServiceImpl extends AbstractCriteriaService<User> implements UserService {

    private final ObjectMapper objectMapper;

    public UserServiceImpl(ObjectMapper objectMapper) {
        super(User.class);
        this.objectMapper=objectMapper;
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
}
