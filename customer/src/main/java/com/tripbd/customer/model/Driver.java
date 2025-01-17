package com.tripbd.customer.model;

import java.io.Serializable;
import java.util.List;

public class Driver implements Serializable {
    String id;
    String name;
    String phoneNumber;
    String password;
    String email;
    String imageUrl;
    String address;
    int totalTrips;
    int completeTrips;
    int Due;
    String fcmToken;
    List<Vehicle> vehicles;
    int due;
    String referCode;

    String fcmArea;

    public Driver() {
    }

    public Driver(String id, String name, String phoneNumber, String password, String email, String imageUrl, String address, int totalTrips, int completeTrips, int due, String fcmToken, List<Vehicle> vehicles, int due1, String referCode, String fcmArea) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.imageUrl = imageUrl;
        this.address = address;
        this.totalTrips = totalTrips;
        this.completeTrips = completeTrips;
        Due = due;
        this.fcmToken = fcmToken;
        this.vehicles = vehicles;
        this.due = due1;
        this.referCode = referCode;
        this.fcmArea = fcmArea;
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
        return Due;
    }

    public void setDue(int due) {
        Due = due;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getFcmArea() {
        return fcmArea;
    }

    public void setFcmArea(String fcmArea) {
        this.fcmArea = fcmArea;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}


