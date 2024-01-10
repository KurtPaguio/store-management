package com.example.storemanagement.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.storemanagement.security.ApiResponseDto;
import com.example.storemanagement.security.JwtGenerator;
import com.example.storemanagement.store.domain.Store;
import com.example.storemanagement.store.domain.StoreRepository;
import com.example.storemanagement.store.domain.StoreType;
import com.example.storemanagement.store.dto.StoreForm;
import com.example.storemanagement.store.service.StoreServiceImpl;
import com.example.storemanagement.user.domain.UserRepository;
import com.example.storemanagement.user.domain.UserRole;
import com.example.storemanagement.user.domain.Users;
import com.example.storemanagement.user.service.UserServiceImpl;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class StoreServiceTest {
    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private JwtGenerator jwtGenerator;

    @Mock
    private UserRepository userRepository;

    private StoreForm form;

    private Store store;

    private Users john;
    private final String TOKEN = "Token-123";

    @Before
    public void setup(){
        form = new StoreForm();
        form.setName("Nike");
        form.setAddress("Philippines");
        form.setTelephoneNumber(BigInteger.valueOf(11112222));
        form.setEmailAddress("nike_test@gmail.com");
        form.setType(StoreType.WAREHOUSE);

        store = new Store(form.getName(), form.getAddress(), form.getTelephoneNumber(), form.getEmailAddress(), form.getType());
        john = new Users("John", "Doe", "Password123_", "johndoe@gmail.com", UserRole.USER);

    }

    @Test
    public void createStore_success(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn("test-user-id");
        when(userRepository.findById("test-user-id")).thenReturn(Optional.ofNullable(john));
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        ResponseEntity<ApiResponseDto> result = storeService.createStore(form, TOKEN);

        assertEquals(HttpStatus.OK, result.getBody().getHttpStatus());
        assertEquals("Store Nike successfully created", "Store " + store.getName() + " successfully created");
    }

    @Test
    public void createStore_invalidToken_internalServerError(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(false);

        ResponseEntity<ApiResponseDto> result = storeService.createStore(form, TOKEN);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getBody().getHttpStatus());
    }
}
