package com.example.storemanagement.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.storemanagement.exceptions.NotFoundException;
import com.example.storemanagement.exceptions.UserRoleException;
import com.example.storemanagement.exceptions.ValidationException;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private final String TEST_ADMIN_ID = "test-admin-id";
    private final String TEST_USER_ID = "test-user-id";

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
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.ofNullable(john));
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

    @Test
    public void updateStore_success(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.ofNullable(john));
        when(storeRepository.findByIdAndUsersId(anyString(), anyString())).thenReturn(store);
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        form.setName("Puma");
        form.setAddress("Manila");
        ResponseEntity<?> result = storeService.updateStore(form, TOKEN, TEST_USER_ID);

        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test(expected = ValidationException.class)
    public void updateStore_noChangesSubmitted_throwValidation(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.ofNullable(john));
        when(storeRepository.findByIdAndUsersId(anyString(), anyString())).thenReturn(store);

        storeService.updateStore(form, TOKEN, TEST_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateStore_userIsNull_throwValidation(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.ofNullable(null));

        storeService.updateStore(form, TOKEN, TEST_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateStore_storeIsNull_throwValidation(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.ofNullable(null));
        when(storeRepository.findByIdAndUsersId(anyString(), anyString())).thenReturn(null);

        storeService.updateStore(form, TOKEN, TEST_USER_ID);
    }

    @Test
    public void deleteStore_success(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.ofNullable(john));
        when(storeRepository.findByIdAndUsersId(anyString(), anyString())).thenReturn(store);

        ResponseEntity<?> result = storeService.deleteStore(TOKEN, TEST_USER_ID);

        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void findAllStores_success(){
        List<Store> stores = Collections.singletonList(store);
        john.setRole(UserRole.ADMIN);

        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_ADMIN_ID);
        when(userRepository.findById(TEST_ADMIN_ID)).thenReturn(Optional.ofNullable(john));
        when(storeRepository.findAll()).thenReturn(stores);

        List<Store> result = storeService.findAllStores(TOKEN);

        assertEquals(1, result.size());
    }

    @Test(expected = UserRoleException.class)
    public void findAllStores_notAdminOrSuperAdmin_throwException(){
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.ofNullable(john));

        storeService.findAllStores(TOKEN);
    }

    @Test
    public void findUserStores_success(){
        List<Store> stores = Collections.singletonList(store);

        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserFromJWT(TOKEN)).thenReturn(TEST_USER_ID);
        when(storeRepository.findAllByUsersId(anyString())).thenReturn(stores);

        List<Store> result = storeService.findUserStores(TOKEN);

        assertEquals(1, result.size());
    }
}
