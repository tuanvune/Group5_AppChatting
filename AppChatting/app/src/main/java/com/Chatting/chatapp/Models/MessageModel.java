package com.Chatting.chatapp.Models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String sender;
    private String message;
    private String imageURL;


    public MessageModel(String sender, String message, String imageURL) {
        this.sender = sender;
        this.message = message;
        this.imageURL = imageURL;
    }

    public MessageModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
