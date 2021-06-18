package com.example.safego.domain;


import java.io.Serializable;

public class ActiveUser implements Serializable {

    private String id;

    private String senderPhoneNumber;

    private String receiverPhoneNumber;

    private boolean isActive;

    public ActiveUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
