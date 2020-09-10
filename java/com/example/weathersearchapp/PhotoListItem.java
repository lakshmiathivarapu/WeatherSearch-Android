package com.example.weathersearchapp;

public class PhotoListItem {

    private String head;
    private String imageUrl;
    public PhotoListItem(String head_item, String imageUrl) {
        this.head = head_item;
        this.imageUrl = imageUrl;
    }
    public String getHead() {
        return head;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}