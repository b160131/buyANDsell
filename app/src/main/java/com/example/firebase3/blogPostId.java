package com.example.firebase3;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

/**
 * Created by root on 15/7/18.
 */

class BlogPostId {

    @Exclude
    public String BlogPostId;

    public <T extends BlogPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;
        return (T) this;
    }

}
