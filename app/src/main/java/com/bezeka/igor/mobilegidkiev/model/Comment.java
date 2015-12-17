package com.bezeka.igor.mobilegidkiev.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Igor on 03.12.2015.
 */
public class Comment {

    public static final String JSON_ID = "id";
    public static final String JSON_TEXT = "text";
    public static final String JSON_RATING = "rating";
    public static final String JSON_NAME = "name";
    public static final String JSON_DATE = "date";

    String id;
    float rating;
    String name;
    String text;
    String date;

    public Comment(JSONObject json) throws JSONException {
        setId(json.getString(JSON_ID));
        setText(json.getString(JSON_TEXT));
        setName(json.getString(JSON_NAME));
        setRating(Float.valueOf(json.getString(JSON_RATING)));
        setDate(json.getString(JSON_DATE));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID,getId());
        json.put(JSON_TEXT,getText());
        json.put(JSON_NAME,getName());
        json.put(JSON_RATING,getRating());
        json.put(JSON_DATE,getDate());

        return json;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
