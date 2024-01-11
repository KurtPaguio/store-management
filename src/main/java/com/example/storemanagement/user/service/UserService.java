package com.example.storemanagement.user.service;


import com.example.storemanagement.security.AuthResponseDto;
import com.example.storemanagement.user.domain.Users;
import com.example.storemanagement.user.dto.LoginForm;
import com.example.storemanagement.user.dto.UserForm;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UserService {

    Users createUser(UserForm form);
    ResponseEntity<AuthResponseDto> login(LoginForm form);
}
