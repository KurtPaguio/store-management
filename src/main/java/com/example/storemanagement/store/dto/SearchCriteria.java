package com.example.storemanagement.store.dto;

import com.example.storemanagement.store.domain.StoreType;

public class SearchCriteria {
    private String search;
    private StoreType type;

    public SearchCriteria(String search, StoreType type) {
        this.search = search;
        this.type = type;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public StoreType getType() {
        return type;
    }

    public void setType(StoreType type) {
        this.type = type;
    }

}
