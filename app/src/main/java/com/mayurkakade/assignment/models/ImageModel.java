package com.mayurkakade.assignment.models;

public class ImageModel {
    long id;
    long title_id;
    String image_uri;

    public ImageModel() {
    }

    public ImageModel(long id, long title_id, String image_uri) {
        this.id = id;
        this.title_id = title_id;
        this.image_uri = image_uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTitle_id() {
        return title_id;
    }

    public void setTitle_id(long title_id) {
        this.title_id = title_id;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
