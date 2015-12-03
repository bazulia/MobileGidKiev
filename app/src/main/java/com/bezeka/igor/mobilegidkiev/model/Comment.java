package com.bezeka.igor.mobilegidkiev.model;

/**
 * Created by Igor on 03.12.2015.
 */
public class Comment {

    String name;
    String text;

    public Comment(String name, String text){
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
