package com.example.storemanagement.store.dto;

import com.example.storemanagement.store.domain.Store;
import com.example.storemanagement.store.domain.StoreType;
import com.example.storemanagement.user.domain.Users;
import com.example.storemanagement.user.dto.UserDto;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreDto {
    private String name;
    private String address;
    private BigInteger telephoneNumber;
    private String emailAddress;
    private StoreType type;
    private UserDto owner;

    public StoreDto(String name, String address, BigInteger telephoneNumber, String emailAddress, StoreType type, Users owner) {
        this.name = name;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.emailAddress = emailAddress;
        this.type = type;
        this.owner = new UserDto(owner);
    }

    public static StoreDto buildFromEntity(Store store){
        return new StoreDto(store.getName(), store.getAddress(), store.getTelephoneNumber(), store.getEmailAddress(), store.getType(), store.getUsers());
    }

    public static List<StoreDto> buildFromEntities(List<Store> stores){
        List<StoreDto> storeDto = new ArrayList<>();

        for(Store store: stores){
            storeDto.add(buildFromEntity(store));
        }

        return storeDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(BigInteger telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StoreType getType() {
        return type;
    }

    public void setType(StoreType type) {
        this.type = type;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void String(UserDto owner) {
        this.owner = owner;
    }
}
