package com.example.storemanagement.store.dto;

import com.example.storemanagement.store.domain.StoreType;
import java.math.BigInteger;

public class StoreForm {
    private String name;
    private String address;
    private BigInteger telephoneNumber;
    private String emailAddress;
    private StoreType type;

    public StoreForm(){}
    public StoreForm(String name, String address, BigInteger telephoneNumber, String emailAddress, StoreType type) {
        this.name = name;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.emailAddress = emailAddress;
        this.type = type;
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
}
