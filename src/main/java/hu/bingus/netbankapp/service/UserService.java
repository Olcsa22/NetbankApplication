package hu.bingus.netbankapp.service;

import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;

import java.util.AbstractMap;

public interface UserService {

    public AbstractMap.SimpleEntry register(UserDTO user);

    public User findByUsername(String username);

}
