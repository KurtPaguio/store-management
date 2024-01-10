package com.example.storemanagement.store.controller;

import com.example.storemanagement.security.ApiResponseDto;
import com.example.storemanagement.security.JwtGenerator;
import com.example.storemanagement.store.domain.Store;
import com.example.storemanagement.store.dto.SearchCriteria;
import com.example.storemanagement.store.dto.StoreDto;
import com.example.storemanagement.store.dto.StoreForm;
import com.example.storemanagement.store.service.StoreService;
import com.example.storemanagement.user.controller.UserController;
import java.util.List;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final StoreService storeService;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public StoreController(StoreService storeService, JwtGenerator jwtGenerator) {
        this.storeService = storeService;
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping("/allStores")
    private ResponseEntity<?> findAllStores(@RequestHeader("Authorization") String header){
        log.info("Getting all stores..");
        String token = header.replace("Bearer ", "");

        try{
            return new ResponseEntity<>(StoreDto.buildFromEntities(storeService.findAllStores(token)), HttpStatus.OK);
        } catch (Exception e){
            log.error("Error getting all stores: {}", e.getMessage());
            return new ResponseEntity<>("Error getting all stores: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userStores")
    private ResponseEntity<?> findUserStores(@RequestHeader("Authorization") String header){
        String token = header.replace("Bearer ", "");

        try{
            return new ResponseEntity<>(StoreDto.buildFromEntities(storeService.findUserStores(token)), HttpStatus.OK);
        } catch (Exception e){
            log.error("Error getting user's stores: {}", e.getMessage());
            return new ResponseEntity<>("Error getting user's stores: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getStore/{storeId}")
    private ResponseEntity<StoreDto> getStoreDetails(@PathVariable("storeId") String storeId){
        return new ResponseEntity<>(StoreDto.buildFromEntity(storeService.getStoreDetails(storeId)), HttpStatus.OK);
    }

    @PostMapping("/searchStore")
    private ResponseEntity<List<StoreDto>> searchStore(@RequestBody SearchCriteria criteria){
        log.info("Searching with criteria {}", criteria);
        return new ResponseEntity<>(storeService.searchStoreByCriteria(criteria), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto> createStore(@RequestBody StoreForm form, @RequestHeader("Authorization") String header){
        String token = header.replace("Bearer ", "");

        try{
            return storeService.createStore(form, token);
        } catch (Exception e){
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR,  "Error creating store: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{storeId}/update")
    public ResponseEntity<?> updateStore(@RequestBody StoreForm form, @RequestHeader("Authorization") String header,
        @PathVariable("storeId") String storeId){
        String token = header.replace("Bearer ", "");

        try{
            return storeService.updateStore(form, token, storeId);
        } catch (Exception e){
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR,  "Error updating store: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{storeId}/delete")
    public ResponseEntity<?> deleteStore(@RequestHeader("Authorization") String header, @PathVariable("storeId") String storeId){
        String token = header.replace("Bearer ", "");

        try{
            return storeService.deleteStore(token, storeId);
        } catch (Exception e){
            return new ResponseEntity<>(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR,  "Error deleting store: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
