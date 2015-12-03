package com.bezeka.igor.mobilegidkiev.model;

/**
 * Created by Igor on 03.12.2015.
 */
public class Type {
    String typeId;
    String name;
    public Type(String typeId, String name){
        this.name = name;
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
