package com.tripbd.customer.model;

public class District {
    String id;
    String division_id;
    String name;
    String bn_name;

    public District() {
    }

    public District(String id, String division_id, String name, String bn_name) {
        this.id = id;
        this.division_id = division_id;
        this.name = name;
        this.bn_name = bn_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDivision_id() {
        return division_id;
    }

    public void setDivision_id(String division_id) {
        this.division_id = division_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBn_name() {
        return bn_name;
    }

    public void setBn_name(String bn_name) {
        this.bn_name = bn_name;
    }
}
