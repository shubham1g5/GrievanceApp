package com.example.shubham.grievanceapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shubham on 28/3/17.
 */

@IgnoreExtraProperties
public class User {
    private String password;

    public User() {
    }

    public User(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
