package com.example.firebase3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class blogDescription extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_description);


        String blog_img= getIntent().getStringExtra("BLOG_IMAGE");
        ImageView blogImage = findViewById(R.id.blogImage);
        Glide.with(this).load(blog_img).into(blogImage);
    }
}
