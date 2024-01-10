package com.example.storemanagement.user.service;

import com.example.storemanagement.exceptions.ValidationException;
import com.example.storemanagement.role.domain.Role;
import com.example.storemanagement.role.domain.RoleRepository;
import com.example.storemanagement.security.AuthResponseDto;
import com.example.storemanagement.security.JwtGenerator;
import com.example.storemanagement.user.domain.UserRepository;
import com.example.storemanagement.user.domain.UserRole;
import com.example.storemanagement.user.dto.LoginForm;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.storemanagement.user.domain.Users;
import com.example.storemanagement.user.dto.UserForm;
import com.example.storemanagement.validate.Validate;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
        RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public List<Users> findAllUsers(){
        return userRepository.findAll();
    }
    @Override
    public Users createUser(UserForm form){
        Validate.notNull(form);
        validateUserForm(form);

        log.info("Creating user with form {}", form);
        Users user = userRepository.findByEmailIgnoreCase(form.getEmail());

        if(user != null){
            throw new ValidationException("User with email " + form.getEmail() + " already exists");
        }

        Role role = roleRepository.findByName(form.getRole().name());

        if(role == null){
            throw new ValidationException("Role does not exist");
        }

        Users saveUser = new Users(form.getFirstName(), form.getLastName(), passwordEncoder.encode(form.getPassword()),
            form.getEmail(), form.getRole());
        saveUser.setRoles(Collections.singletonList(role));

        log.info("Done creating for user {}", form.getEmail());
        return userRepository.save(saveUser);
    }

    private void validateUserForm(UserForm form){
        validateName(form.getFirstName(), form.getLastName());
        validatePassword(form.getPassword());
        validateEmail(form.getEmail());
        validateRole(form.getRole());
    }

    private void validateName(String firstName, String lastName){
        boolean isFirstNameNotValid = Validate.hasIntegersAndSpecialCharacters(firstName);
        boolean isLastNameNotValid = Validate.hasIntegersAndSpecialCharacters(lastName);

        if(isFirstNameNotValid || isLastNameNotValid){
            throw new ValidationException("First name or Last name is not valid! Please try again");
        }
    }

    private void validatePassword(String password){
        boolean hasIntegerAndSpecialCharacter = Validate.hasIntegersAndSpecialCharacters(password);

        if(!hasIntegerAndSpecialCharacter){
            throw new ValidationException("Password must contain at least One(1) number and special character");
        }

        if(password.length() < 10 || password.length() > 20){
            throw new ValidationException("Password must be 10-20 characters");
        }
    }

    private void validateEmail(String email){
        boolean hasCorrectEmailFormat = Validate.hasCorrectEmailFormat(email);

        if(!hasCorrectEmailFormat){
            throw new ValidationException("Email format is not correct. Please try again");
        }
    }

    private void validateRole(UserRole role){
        if(!UserRole.getAllValues().contains(role)){
            throw new ValidationException("Role is invalid! Choose between User, Admin, and Superuser");
        }
    }

    @Override
    public ResponseEntity<AuthResponseDto> login(LoginForm form){
        Validate.notNull(form);
        log.info("Logging user {} into the system", form.getEmail());

        Users user = userRepository.findByEmail(form.getEmail());

        if(user == null){
            throw new ValidationException("User does not exist");
        }

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), form.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtGenerator.generateAccessToken(auth);

        log.info("User {} successfully logged in", form.getEmail());
        return new ResponseEntity<>(new AuthResponseDto(HttpStatus.OK, token, "Logged in successfully with a created token"), HttpStatus.OK);
    }
}
