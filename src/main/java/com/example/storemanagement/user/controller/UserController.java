package com.example.storemanagement.user.controller;

import com.example.storemanagement.security.ApiResponseDto;
import com.example.storemanagement.security.AuthResponseDto;
import com.example.storemanagement.security.JwtGenerator;
import com.example.storemanagement.user.dto.LoginForm;
import com.example.storemanagement.user.dto.UserDto;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.storemanagement.user.domain.Users;
import com.example.storemanagement.user.dto.UserForm;
import com.example.storemanagement.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUsers")
    public List<UserDto> getUsers(){
        return UserDto.buildFromEntities(userService.findAllUsers());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto> createUser(@RequestBody UserForm form){
        try{
            Users user = userService.createUser(form);
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.OK, "User " + user.getEmail() + " successfully created"), HttpStatus.OK);
        } catch (Exception e){
            log.error("Error creating user: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm form){
        try {
            return userService.login(form);
        } catch (Exception e){
            log.error("Error logging into the system: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error logging in: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
