package com.example.safego.domain;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String id;
    private String name;

    private String phoneNumber;

    private String age;

    private String sex;

    public UserInfo()
    {

    }
    public UserInfo(String name, String phoneNumber, String age, String sex) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
