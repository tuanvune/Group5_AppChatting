package com.Chatting.chatapp.Models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String username;
    private String email;
    private String phone;
    private String password;
    private String imageURL;
    private String status;

    public UserModel(String username, String email, String phone, String password, String imageURL) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.imageURL = imageURL;
    }

    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
