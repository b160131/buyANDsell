package com.example.firebase3;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by root on 19/8/18.
 */

public class blogRecyclerData {

    private Context context;
    private RecyclerView blog_list_View;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    public static ArrayList<BlogPost> blog_list = new ArrayList<>();

    public blogRecyclerData(final RecyclerView blog_list_View, final Context context) {

        this.context = context;
        this.blog_list_View = blog_list_View;

        blog_list.clear();

        firebaseFirestore = FirebaseFirestore.getInstance();

        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING);

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {


                   blog_list.clear();

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {


                        if (doc.getType() == DocumentChange.Type.ADDED) {


                            final String blogPostId = doc.getDocument().getId();

                            final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);


                            blog_list.add(blogPost);


                        }
                    }// for loop
                    BlogRecyclerAdapter blogRecyclerAdapter=new BlogRecyclerAdapter(context, blog_list);
                    LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context);
                    blog_list_View.setLayoutManager(linearLayoutManager);
                    blog_list_View.setHasFixedSize(true);

                    blog_list_View.setAdapter(blogRecyclerAdapter);




                }


            }
        });


    }




}



