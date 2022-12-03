package com.vehicle.customer.model;

public class Address {
    String name;
    String thana;
    String district;

    public Address(String name, String thana, String district) {
        this.name = name;
        this.thana = thana;
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
