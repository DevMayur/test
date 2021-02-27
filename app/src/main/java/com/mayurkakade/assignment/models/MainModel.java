package com.mayurkakade.assignment.models;

public class MainModel {
    String title;
    Long title_id;

    public MainModel() {
    }

    public MainModel(String title, Long title_id) {
        this.title = title;
        this.title_id = title_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTitle_id() {
        return title_id;
    }

    public void setTitle_id(Long title_id) {
        this.title_id = title_id;
    }
}
