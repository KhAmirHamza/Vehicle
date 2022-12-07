package com.vehicle.driver.model;

import androidx.annotation.Nullable;

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
