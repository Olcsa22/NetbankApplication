package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import org.springframework.stereotype.Service;

@Service
public class HomeServiceImpl extends AbstractCriteriaService<User> implements HomeService {

    public HomeServiceImpl() {
        super(User.class);
    }

    @Override
    public JsonNode register(UserDTO user) {
        //JsonNode response =
        return null;
    }
}
