package com.example.safego.domain;


import java.io.Serializable;

public class UserAuth implements Serializable {


    private String phoneNumber;

    private String password;

    public UserAuth() {
    }

    public UserAuth(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
