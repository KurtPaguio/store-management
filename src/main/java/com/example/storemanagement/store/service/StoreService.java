package com.example.storemanagement.store.service;

import com.example.storemanagement.security.ApiResponseDto;
import com.example.storemanagement.store.domain.Store;
import com.example.storemanagement.store.dto.SearchCriteria;
import com.example.storemanagement.store.dto.StoreDto;
import com.example.storemanagement.store.dto.StoreForm;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface StoreService {
    Store getStoreDetails(String storeId);
    ResponseEntity<ApiResponseDto> createStore(StoreForm form, String token);
    ResponseEntity<?> updateStore(StoreForm form, String token, String storeId);
    ResponseEntity<?> deleteStore(String token, String storeId);
    List<Store> findAllStores(String token);
    List<Store> findUserStores(String token);
    List<StoreDto> searchStoreByCriteria(SearchCriteria criteria);
}
