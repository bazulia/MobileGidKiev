package com.bezeka.igor.mobilegidkiev.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Igor on 03.12.2015.
 */
public class Comment {

    public static final String JSON_ID = "id";
    public static final String JSON_PLACE_ID = "place_id";
    public static final String JSON_USER_ID = "user_id";
    public static final String JSON_NAME = "name";
    public static final String JSON_TEXT = "text";

    String id;
    String placeId;
    String userId;
    String name;
    String text;

    public Comment(JSONObject json) throws JSONException {
        setId(json.getString(JSON_ID));
        setPlaceId(json.getString(JSON_PLACE_ID));
        setUserId(json.getString(JSON_USER_ID));
        setText(json.getString(JSON_TEXT));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID,getId());
        json.put(JSON_PLACE_ID,getPlaceId());
        json.put(JSON_USER_ID,getUserId());
        json.put(JSON_TEXT,getText());
        return json;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
