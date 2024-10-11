package com.tripbd.driver.model;

import androidx.annotation.Nullable;

import java.util.List;

public class Driver {
    String id;
    String name;
    String phoneNumber;
    String password;
    String email;
    String imageUrl;
    String address;
    int totalTrips;
    int completeTrips;
    int due;
    String referCode;
    List<Vehicle> vehicles;
    String fcmToken;
    String fcmLoadingArea;
    String fcmUnloadingArea;

    public Driver() {
    }

/*    public Driver(String id, String name, String phoneNumber, String email, String imageUrl, String address, int totalTrips, int completeTrips) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.imageUrl = imageUrl;
        this.address = address;
        this.totalTrips = totalTrips;
        this.completeTrips = completeTrips;
    }*/

    public Driver(@Nullable String id, String name, String phoneNumber, String password, String email,
                  String imageUrl, String address, int totalTrips, int completeTrips, int due,
                  List<Vehicle> vehicles, String referCode, String fcmToken, String fcmLoadingArea, String fcmUnloadingArea) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.imageUrl = imageUrl;
        this.address = address;
        this.totalTrips = totalTrips;
        this.completeTrips = completeTrips;
        this.due = due;
        this.vehicles = vehicles;
        this.referCode = referCode;
        this.fcmToken = fcmToken;
        this.fcmLoadingArea = fcmLoadingArea;
        this.fcmUnloadingArea = fcmUnloadingArea;
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

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public int getCompleteTrips() {
        return completeTrips;
    }

    public void setCompleteTrips(int completeTrips) {
        this.completeTrips = completeTrips;
    }

    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }
/*
    public List<Vehicle> getCars() {
        return vehicles;
    }

    public void setCars(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }*/

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmLoadingArea() {
        return fcmLoadingArea;
    }

    public void setFcmLoadingArea(String fcmLoadingArea) {
        this.fcmLoadingArea = fcmLoadingArea;
    }

    public String getFcmUnloadingArea() {
        return fcmUnloadingArea;
    }

    public void setFcmUnloadingArea(String fcmUnloadingArea) {
        this.fcmUnloadingArea = fcmUnloadingArea;
    }
}


