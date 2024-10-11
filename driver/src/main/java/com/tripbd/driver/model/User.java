package com.tripbd.driver.model;

import androidx.annotation.Nullable;

public class User {

    String id;
    String name;
    String phoneNumber;
    String password;
    String email;
    String imageUrl;
    String address;

    public User() {
    }

    public User(@Nullable String id, @Nullable String name, String phoneNumber, String password, @Nullable String email, @Nullable String imageUrl, @Nullable String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.imageUrl = imageUrl;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
