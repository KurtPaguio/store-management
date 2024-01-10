package com.example.storemanagement.user.domain;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    USER("User"),
    ADMIN("Admin"),
    SUPERUSER("Superuser");

    String label;
    UserRole(String label){this.label = label;}

    public static List<UserRole> getAllValues(){
        return Arrays.asList(USER, ADMIN, SUPERUSER);
    }
}
