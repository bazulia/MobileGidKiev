package com.bezeka.igor.mobilegidkiev.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Igor on 25.11.2015.
 */
public class Place {

    public static final String JSON_ID = "id";
    public static final String JSON_TITLE = "title";
    public static final String JSON_DESCRIPTION = "description";
    public static final String JSON_ADDRESS = "address";
    public static final String JSON_LINK_IMAGE = "link_image";
    public static final String JSON_WORK_TIME = "work_time";
    public static final String JSON_REGION = "region";
    public static final String JSON_NAME = "name";
    public static final String JSON_PARAMS = "params";
    public static final String JSON_AVG_RATE = "avgRate";
    public static final String JSON_COMMENT_COUNT = "rateCount";
    public static final String JSON_RATE_COUNT = "rateCount";

    String id;
    String title;
    String description;
    String imgLink;
    float rating;
    String address;
    String work_time;
    String type;
    String region;
    String countComments;
    String name;
    double distance;

    public Place(JSONObject json) throws JSONException {
        setId(json.getString(JSON_ID));
        setTitle(json.getString(JSON_TITLE));
        setDescription(json.getString(JSON_DESCRIPTION));
        setAddress(json.getString(JSON_ADDRESS));
        setImgLink(json.getString(JSON_LINK_IMAGE));
        setWork_time(json.getString(JSON_WORK_TIME));
        setRegion(json.getString(JSON_REGION));
        setCountComments(json.getString(JSON_COMMENT_COUNT));
        setRating(Float.valueOf(json.getString(JSON_AVG_RATE)));
        setName(json.getString(JSON_NAME));

    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, getId());
        json.put(JSON_TITLE, getTitle());
        json.put(JSON_DESCRIPTION, getDescription());
        json.put(JSON_ADDRESS, getAddress());
        json.put(JSON_LINK_IMAGE, getImgLink());
        json.put(JSON_WORK_TIME, getWork_time());
        json.put(JSON_REGION, getRegion());
        json.put(JSON_COMMENT_COUNT, getCountComments());
        json.put(JSON_AVG_RATE, getRating());
        json.put(JSON_NAME, getName());
        return json;
    }

    public Place(String id,String title, String description, String imgLink, float rating,
                 String address, String work_time, String type, String region, double distance){
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgLink = imgLink;
        this.rating = rating;
        this.address = address;
        this.work_time = work_time;
        this.type = type;
        this.region = region;
        this.distance = distance;
    }

    private static float round(float number, int scale) {
        int pow = (int) Math.pow(10, scale);
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountComments() {
        return countComments;
    }

    public void setCountComments(String countComments) {
        this.countComments = countComments;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public float getRating() {
        rating = round(rating,1);
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }
}
