package com.bezeka.igor.mobilegidkiev;

/**
 * Created by Igor on 25.11.2015.
 */
public class Place {
    String id;
    String title;
    String description;
    String imgLink;
    float rating;
    String address;
    String work_time;
    String type;
    String region;

    public Place(String id,String title, String description, String imgLink, float rating,
                 String address, String work_time, String type, String region){
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgLink = imgLink;
        this.rating = rating;
        this.address = address;
        this.work_time = work_time;
        this.type = type;
        this.region = region;
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
