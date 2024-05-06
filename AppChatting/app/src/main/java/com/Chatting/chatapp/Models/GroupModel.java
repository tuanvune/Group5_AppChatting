package com.Chatting.chatapp.Models;

import java.io.Serializable;

public class GroupModel implements Serializable {

    private String name;
    private String imageURL;

    public GroupModel(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public GroupModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
