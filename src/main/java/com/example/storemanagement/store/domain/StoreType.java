package com.example.storemanagement.store.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StoreType {
    ALL("All"),
    DEPARTMENT_STORE("Department Store"),
    SUPERMARKET("Supermarket"),
    CONVENIENCE_STORE("Convenience Store"),
    WAREHOUSE("Warehouse"),
    BOOKSTORE("Bookstore");

    String label;

    StoreType(String label){this.label = label;};

    public String getLabel(){return label;}

    public static List<StoreType> getAllValues(){
        return Arrays.asList(DEPARTMENT_STORE, SUPERMARKET, CONVENIENCE_STORE, WAREHOUSE, BOOKSTORE);
    }

    public static List<String> getAllLabels(){
        List<String> labels = new ArrayList<>();
        List<StoreType> values = getAllValues();

        for(StoreType storeType: values){
            labels.add(storeType.label);
        }

        return labels;
    }
}
