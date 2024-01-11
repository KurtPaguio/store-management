package com.example.storemanagement.user;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.storemanagement.exceptions.ValidationException;
import com.example.storemanagement.role.domain.Role;
import com.example.storemanagement.role.domain.RoleRepository;
import com.example.storemanagement.security.AuthResponseDto;
import com.example.storemanagement.security.JwtGenerator;
import com.example.storemanagement.user.domain.UserRepository;
import com.example.storemanagement.user.domain.UserRole;
import com.example.storemanagement.user.domain.Users;
import com.example.storemanagement.user.dto.LoginForm;
import com.example.storemanagement.user.dto.UserForm;
import com.example.storemanagement.user.service.UserServiceImpl;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.jws.soap.SOAPBinding.Use;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtGenerator jwtGenerator;

    private UserForm form;

    private LoginForm loginForm;

    private Users john;

    @Before
    public void setUp(){
        form = new UserForm();
        form.setFirstName("John");
        form.setLastName("Doe");
        form.setEmail("johndoe@gmail.com");
        form.setPassword("test12345_!");
        form.setRole(UserRole.USER);

        john = new Users("John", "Doe", "Password123_", "johndoe@gmail.com", UserRole.USER);

        loginForm = new LoginForm("test-email@gmail.com", "Pogiako123_");
    }
    @Test
    public void createUser_success(){
        Users user = new Users(form.getFirstName(), form.getLastName(), form.getPassword(), form.getEmail(), form.getRole());
        Role role = new Role("Admin", "Test");

        when(roleRepository.findByName(anyString())).thenReturn(role);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(null);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        Users result = userService.createUser(form);

        assertEquals(result.getFirstName(), form.getFirstName());
        assertEquals(result.getLastName(), form.getLastName());
        assertEquals(result.getEmail(), form.getEmail());
    }

    @Test
    public void login_success(){
        Authentication authentication = mock(Authentication.class);

        when(userRepository.findByEmail(anyString())).thenReturn(john);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtGenerator.generateAccessToken(any(Authentication.class))).thenReturn("Token-Test");

        ResponseEntity<AuthResponseDto> result = userService.login(loginForm);

        assertEquals(HttpStatus.OK, result.getBody().getStatus());
        assertEquals("Token-Test", result.getBody().getAccessToken());
    }

    @Test(expected = ValidationException.class)
    public void login_userNull_throwException(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        userService.login(loginForm);
    }
}
