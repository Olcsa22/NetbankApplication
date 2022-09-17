package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.model.dto.UserDTO;

public interface HomeService {

    public JsonNode register(UserDTO user);

}
