package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;
import org.springframework.http.HttpStatus;

import java.util.AbstractMap;

public interface RoleService {
    void addUser(UserDTO user);
    AbstractMap.SimpleEntry<JsonNode, HttpStatus> addAdmin(String user);
}
