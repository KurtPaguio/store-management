package com.example.storemanagement.user.dto;

import com.example.storemanagement.role.domain.Role;
import com.example.storemanagement.user.domain.UserRole;
import java.util.ArrayList;
import java.util.Date;
import com.example.storemanagement.user.domain.Users;
import java.util.List;

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private Date dateCreated;
    private Date dateModified;

    public UserDto(String firstName, String lastName, String email, UserRole role,
        Date dateCreated, Date dateModified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public UserDto(Users user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.dateCreated = user.getDateCreated();
        this.dateModified = user.getDateModified();
    }

    public static UserDto buildFromEntity(Users user){
        return new UserDto(user);
    }

    public static List<UserDto> buildFromEntities(List<Users> users){
        List<UserDto> usersDto = new ArrayList<>();

        for(Users user: users){
            usersDto.add(buildFromEntity(user));
        }

        return usersDto;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
}
