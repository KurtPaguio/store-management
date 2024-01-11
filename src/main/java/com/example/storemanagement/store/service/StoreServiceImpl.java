package com.example.storemanagement.store.service;

import com.example.storemanagement.exceptions.NotFoundException;
import com.example.storemanagement.exceptions.TokenException;
import com.example.storemanagement.exceptions.UserRoleException;
import com.example.storemanagement.exceptions.ValidationException;
import com.example.storemanagement.security.ApiResponseDto;
import com.example.storemanagement.security.JwtGenerator;
import com.example.storemanagement.store.domain.SearchSpecs;
import com.example.storemanagement.store.domain.Store;
import com.example.storemanagement.store.domain.StoreRepository;
import com.example.storemanagement.store.domain.StoreType;
import com.example.storemanagement.store.dto.SearchCriteria;
import com.example.storemanagement.store.dto.StoreDto;
import com.example.storemanagement.store.dto.StoreForm;
import com.example.storemanagement.user.domain.UserRepository;
import com.example.storemanagement.user.domain.UserRole;
import com.example.storemanagement.user.domain.Users;
import com.example.storemanagement.user.service.UserServiceImpl;
import com.example.storemanagement.validate.Validate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StoreServiceImpl implements StoreService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final StoreRepository storeRepository;
    private final JwtGenerator tokenGenerator;
    private final UserRepository userRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, JwtGenerator jwtGenerator, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.tokenGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }

    @Override
    public Store getStoreDetails(String storeId) {
        log.info("Getting details of store with id {}", storeId);

        return storeRepository.findById(storeId).orElseThrow(() -> new ValidationException("Store does not exist"));
    }

    @Override
    public ResponseEntity<ApiResponseDto> createStore(StoreForm form, String token){
        Validate.notNull(form);
        validateStoreForm(form);
        validateExistingStore(form);

        if(!tokenGenerator.validateToken(token)){
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "Token not already valid. Please login again"),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Creating store with form {} using token {}", form, token);

        String userId = tokenGenerator.getUserFromJWT(token);
        Store store = new Store(form.getName(), form.getAddress(), form.getTelephoneNumber(), form.getEmailAddress(), form.getType());
        store.setUsers(userRepository.findById(userId).orElse(null));
        storeRepository.save(store);

        log.info("Successfully saved store");
        return new ResponseEntity<>(new ApiResponseDto(HttpStatus.OK, "Store " + store.getName() + " successfully created"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateStore(StoreForm form, String token, String storeId) {
        Validate.notNull(form);
        validateStoreForm(form);

        if(!tokenGenerator.validateToken(token)){
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "Token not already valid. Please login again"),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Updating details for store id {}", storeId);

        String userId = tokenGenerator.getUserFromJWT(token);
        Users user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        Store store = null;

        if(user.isAdminOrSuperAdmin()){
            store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException("Store not found"));
        } else{
            store = storeRepository.findByIdAndUsersId(storeId, userId);

            if(store == null){
                throw new NotFoundException("Store does not exist nor owned by the user");
            }
        }

        if(validateFormHasNoChanges(form, store)){
            throw new ValidationException("No changes detected. Please update at least one field");
        }

        store.setName(form.getName());
        store.setAddress(form.getAddress());
        store.setEmailAddress(form.getEmailAddress());
        store.setTelephoneNumber(form.getTelephoneNumber());
        store.setType(form.getType());

        storeRepository.save(store);

        log.info("Successfully updated store");
        return new ResponseEntity<>(new ApiResponseDto(HttpStatus.OK, "Store " + store.getName() + " updated successfully"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteStore(String token, String storeId) {
        if(!tokenGenerator.validateToken(token)){
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "Token not already valid. Please login again"),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Deleting store with id {}", storeId);

        String userId = tokenGenerator.getUserFromJWT(token);
        Users user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        Store store = null;

        if(user.isAdminOrSuperAdmin()){
            store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException("Store not found"));
        } else{
            store = storeRepository.findByIdAndUsersId(storeId, userId);

            if(store == null){
                throw new NotFoundException("Store does not exist nor owned by the user");
            }
        }

        storeRepository.delete(store);

        log.info("Successfully deleted store");
        return new ResponseEntity<>(new ApiResponseDto(HttpStatus.OK, "Store " + store.getName() + " deleted successfully"), HttpStatus.OK);
    }

    @Override
    public List<Store> findAllStores(String token) {
        if(!tokenGenerator.validateToken(token)){
            throw new TokenException("Token not already valid. Please login again");
        }

        String adminId = tokenGenerator.getUserFromJWT(token);
        Users admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("User not found with id: " + adminId));

        if(!(admin.isAdminOrSuperAdmin())){
            throw new UserRoleException("Only Administrators and Super Administrators can access this feature");
        }

        return storeRepository.findAll();
    }

    @Override
    public List<Store> findUserStores(String token) {
        if(!tokenGenerator.validateToken(token)){
            throw new TokenException("Token not already valid. Please login again");
        }

        String userId = tokenGenerator.getUserFromJWT(token);

        log.info("Getting stores owned by user with id {}", userId);
        return storeRepository.findAllByUsersId(userId);
    }

    @Override
    public List<StoreDto> searchStoreByCriteria(SearchCriteria criteria) {
        Specification<Store> specs = buildCriteria(criteria);
        List<Store> stores = storeRepository.findAll(specs);
        List<StoreDto> storeDto = new ArrayList<>();

        for(Store store: stores){
            storeDto.add(StoreDto.buildFromEntity(store));
        }

        return storeDto;
    }

    private Specification<Store> buildCriteria(SearchCriteria criteria){
        log.info("Building search specification..");

        String search = criteria.getSearch();
        Specification<Store> specs;

        if(criteria.getType().equals(StoreType.ALL)){
            specs = Specification.where(SearchSpecs.hasAllType());
        } else {
            specs = Specification.where(SearchSpecs.hasSpecificType(criteria.getType()));
        }

        if(StringUtils.hasText(search)){
            Specification<Store> searchSpecs;
            searchSpecs = Specification.where(SearchSpecs.searchByOwnerDetails(search))
                .or(SearchSpecs.searchByStoreDetails(search));

            specs = specs.and(searchSpecs);
        }

        return specs;
    }
    private void validateStoreForm(StoreForm form){
        validateType(form.getType());
        validateEmail(form.getEmailAddress());
    }

    private void validateExistingStore(StoreForm form){
        Store storeByEmail = storeRepository.findByEmailAddress(form.getEmailAddress());
        Store storeByName = storeRepository.findByName(form.getName());

        if(storeByEmail != null){
            throw new ValidationException("Store with the same email address already exists!");
        }

        if(storeByName != null){
            throw new ValidationException("Store with the same name already exists!");
        }

    }

    private void validateType(StoreType type){
        if(!StoreType.getAllValues().contains(type)){
            throw new ValidationException("Store type invalid!");
        }
    }

    private void validateEmail(String email){
        boolean hasCorrectEmailFormat = Validate.hasCorrectEmailFormat(email);

        if(!hasCorrectEmailFormat){
            throw new ValidationException("Email format is not correct. Please try again");
        }
    }

    private boolean validateFormHasNoChanges(StoreForm form, Store store){
        return areObjectsEqual(form.getName(), store.getName()) &&
            areObjectsEqual(form.getEmailAddress(), store.getEmailAddress()) &&
            areObjectsEqual(form.getTelephoneNumber(), store.getTelephoneNumber()) &&
            areObjectsEqual(form.getAddress(), store.getAddress()) &&
            areObjectsEqual(form.getType(), store.getType());
    }

    private boolean areObjectsEqual(Object object1, Object object2){
        return object1.equals(object2);
    }
}
