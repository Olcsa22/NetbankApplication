package hu.bingus.netbankapp.service;

import hu.bingus.netbankapp.exceptions.EntityAlreadyExistsException;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;

public interface UserService {

    public Boolean register(UserDTO user) throws EntityAlreadyExistsException;

    public User findByUsername(String username);

}
