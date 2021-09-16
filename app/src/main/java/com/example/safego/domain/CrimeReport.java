package com.example.safego.domain;

import java.io.Serializable;

public class CrimeReport implements Serializable {
    private String id;

    private String crimeType;

    private String area;

    private double latitude;

    private double longitude;

    private String time;

    private String victimAge;


    private String victimSex;

    private String phoneNumber;

    public CrimeReport() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVictimAge() {
        return victimAge;
    }

    public void setVictimAge(String victimAge) {
        this.victimAge = victimAge;
    }

    public String getVictimSex() {
        return victimSex;
    }

    public void setVictimSex(String victimSex) {
        this.victimSex = victimSex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
