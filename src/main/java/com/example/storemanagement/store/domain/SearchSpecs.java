package com.example.storemanagement.store.domain;

import com.example.storemanagement.user.domain.Users;
import javax.persistence.criteria.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public class SearchSpecs {
    private static final Logger log = LoggerFactory.getLogger(SearchSpecs.class);

    public static Specification<Store> hasAllType(){
        log.info("Creating specification for ALL types");

        return (root, query, criteriaBuilder) ->root.get("type").in(StoreType.getAllValues());
    }

    public static Specification<Store> hasSpecificType(StoreType type){
        log.info("Creating specification for {} store type", type.label);

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Store> searchByOwnerDetails(String search){
        log.info("Creating specification for search by owner details");

        return (root, query, criteriaBuilder) ->{
            Join<Store, Users> usersJoin = root.join("users");

            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(usersJoin.get("firstName")), "%"+search.toLowerCase()+"%"),
                criteriaBuilder.like(criteriaBuilder.lower(usersJoin.get("lastName")), "%"+search.toLowerCase()+"%"),
                criteriaBuilder.like(criteriaBuilder.lower(usersJoin.get("email")), "%"+search.toLowerCase()+"%")
            );
        };
    }

    public static Specification<Store> searchByStoreDetails(String search){
        log.info("Creating specification for search by store details");
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%"+search.toLowerCase()+"%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("emailAddress")), "%"+search.toLowerCase()+"%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%"+search.toLowerCase()+"%")
            );
        };
    }
}
