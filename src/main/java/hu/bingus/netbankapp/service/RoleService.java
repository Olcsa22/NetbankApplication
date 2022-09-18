package hu.bingus.netbankapp.service;

import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;

import java.util.AbstractMap;

public interface RoleService {
    void addUser(UserDTO user);
    void addAdmin(String user);
}
