package com.example.firebase3;

/**
 * Created by root on 20/7/18.
 */

public class UserDetails {

    public UserDetails()
    {

    }

    public UserDetails(String name, String image) {
        this.name = name;
        this.image = image;
    }

    private String name;
    private String image;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
