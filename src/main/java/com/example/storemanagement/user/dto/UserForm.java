package com.example.storemanagement.user.dto;


import com.example.storemanagement.role.domain.Role;
import com.example.storemanagement.user.domain.UserRole;

public class UserForm {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private UserRole role;

    public UserForm(){}
    public UserForm(String firstName, String lastName, String password, String email, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
