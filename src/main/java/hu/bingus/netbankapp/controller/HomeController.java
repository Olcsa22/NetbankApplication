package hu.bingus.netbankapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.dto.UserDTO;
import hu.bingus.netbankapp.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller(value = "/")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final ObjectMapper objectMapper;

    public ResponseEntity register(@RequestBody UserDTO user) throws JsonProcessingException {

        JsonNode response = homeService.register(user);

        return new ResponseEntity(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(response),HttpStatus.OK);
    }

}
