package com.vehicle.driver.model;

import androidx.annotation.Nullable;

public class Vehicle {
    String id;
    String model; //every vehicle has own model name
    String type; // for truck,pickup,trailer..etc...
    String variety; //open, covered, ac, non ac
    String seat; //micro,bus,private car seat
    String size; // for truck,pickup,trailer..etc...
    String vehicleImageUrl;
    String brtaDocumentImageUrl;
    String nidImageUrl;
    String metro;
    String serial;
    String number;
    String year;
    String driverMobileNumber;

    public Vehicle() {

    }

    public Vehicle( @Nullable String id, String model, String type, String variety, String seat, String size, String metro, String serial, String number, String year,  String vehicleImageUrl, String brtaDocumentImageUrl, String nidImageUrl, String driverMobileNumber) {
        this.id = id;
        this.model = model;
        this.type = type;
        this.variety = variety;
        this.seat = seat;
        this.size = size;
        this.vehicleImageUrl = vehicleImageUrl;
        this.brtaDocumentImageUrl = brtaDocumentImageUrl;
        this.nidImageUrl = nidImageUrl;
        this.metro = metro;
        this.serial = serial;
        this.number = number;
        this.year = year;
        this.driverMobileNumber = driverMobileNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public String getBrtaDocumentImageUrl() {
        return brtaDocumentImageUrl;
    }

    public void setBrtaDocumentImageUrl(String brtaDocumentImageUrl) {
        this.brtaDocumentImageUrl = brtaDocumentImageUrl;
    }

    public String getNidImageUrl() {
        return nidImageUrl;
    }

    public void setNidImageUrl(String nidImageUrl) {
        this.nidImageUrl = nidImageUrl;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDriverMobileNumber() {
        return driverMobileNumber;
    }

    public void setDriverMobileNumber(String driverMobileNumber) {
        this.driverMobileNumber = driverMobileNumber;
    }
}
