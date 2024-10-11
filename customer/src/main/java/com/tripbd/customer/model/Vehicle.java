package com.tripbd.customer.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

/*
public class Vehicle {
    String id;
    String Model;
    String type;
    String openOrCovered;
    int capacity;
    int size;
    int length;
    int weight;
    int height;
    String imageUrl;
    String documentImageUrl;
    String licenceImageUrl;
    String metro;
    String serial;
    String number;

    public Vehicle() {
    }

    public Vehicle(String id, String type, String openOrCovered, int size, int capacity, int length, int weight, int height) {
        this.id = id;
        this.type = type;
        this.openOrCovered = openOrCovered;
        this.size = size;
        this.capacity = capacity;
        this.length = length;
        this.weight = weight;
        this.height = height;
    }

    public Vehicle(@Nullable String id, String model, String type, String openOrCovered, int size, int capacity, int length, int weight, int height, String metro, String serial, String number, String imageUrl, String documentImageUrl) {
        this.id = id;
        Model = model;
        this.type = type;
        this.openOrCovered = openOrCovered;
        this.size = size;
        this.capacity = capacity;
        this.length = length;
        this.weight = weight;
        this.height = height;
        this.imageUrl = imageUrl;
        this.documentImageUrl = documentImageUrl;
        this.metro = metro;
        this.serial = serial;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenOrCovered() {
        return openOrCovered;
    }

    public void setOpenOrCovered(String openOrCovered) {
        this.openOrCovered = openOrCovered;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDocumentImageUrl() {
        return documentImageUrl;
    }

    public void setDocumentImageUrl(String documentImageUrl) {
        this.documentImageUrl = documentImageUrl;
    }

    public String getLicenceImageUrl() {
        return licenceImageUrl;
    }

    public void setLicenceImageUrl(String licenceImageUrl) {
        this.licenceImageUrl = licenceImageUrl;
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
}
*/

public class Vehicle implements Serializable {
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

