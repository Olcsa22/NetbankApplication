package hu.bingus.netbankapp.service;

import hu.bingus.netbankapp.model.dto.UserDTO;

public interface RoleService {
    void addUser(UserDTO user);
    Boolean addAdmin(String user);
}
