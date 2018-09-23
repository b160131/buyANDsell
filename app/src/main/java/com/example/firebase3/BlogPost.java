package com.example.firebase3;

import java.util.Date;

/**
 * Created by root on 13/7/18.
 */

public class BlogPost extends BlogPostId {

    public  String desc,imageUrl,user_id,username,userimage;

    public Date timestamp;


    public BlogPost(String desc, String imageUrl, String user_id, Date timestamp,String username,String userimage) {
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.username=username;
        this.userimage=userimage;
    }

    public  BlogPost(){

    }


    public String getUser_name() {
        return username;
    }

    public void setUser_name(String user_name) {
        this.username = user_name;
    }

    public String getUser_image() {
        return userimage;
    }

    public void setUser_image(String user_image) {
        this.userimage = user_image;
    }




    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String image_id) {
        this.user_id = image_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
