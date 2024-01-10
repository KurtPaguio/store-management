package com.example.storemanagement.store.domain;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>, JpaSpecificationExecutor<Store> {
    Store findByEmailAddress(String email);
    Store findByName(String name);
    List<Store> findAllByUsersId(String userId);
    @Override
    List<Store> findAll(Specification<Store> specification);

    @Query(value = "SELECT * FROM store WHERE id = :storeId AND users_id = :userId", nativeQuery = true)
    Store findByIdAndUsersId(@Param("storeId") String storeId, @Param("userId") String userId);
}
