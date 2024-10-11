package com.tripbd.user.model;

public class Division {
    String id;
    String name;
    String bn_name;

    public Division() {
    }

    public Division(String id, String name, String bn_name) {
        this.id = id;
        this.name = name;
        this.bn_name = bn_name;
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

    public String getBn_name() {
        return bn_name;
    }

    public void setBn_name(String bn_name) {
        this.bn_name = bn_name;
    }
}
