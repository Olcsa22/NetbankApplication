package hu.bingus.netbankapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.dto.UserDTO;
import hu.bingus.netbankapp.service.UserServiceImpl;
import hu.bingus.netbankapp.service.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.AbstractMap;

@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
@PreAuthorize("denyall")
public class UserController {

    private final UserServiceImpl userService;

    private final RoleServiceImpl roleService;

    private final ObjectMapper objectMapper;

    @RequestMapping(value = "/register", produces = "application/json",method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody UserDTO user) throws JsonProcessingException {
        AbstractMap.SimpleEntry<JsonNode,HttpStatus> response = userService.register(user);
        roleService.addUser(user);
        return new ResponseEntity(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(response.getKey()),response.getValue());
    }


    @RequestMapping(value = "/makeAdmin", produces = "application/json",method = RequestMethod.POST)
    public ResponseEntity makeAdmin(String username){

        AbstractMap.SimpleEntry<JsonNode,HttpStatus> response = roleService.addAdmin(username);
        return new ResponseEntity<>(response.getKey(),response.getValue());
    }

}
