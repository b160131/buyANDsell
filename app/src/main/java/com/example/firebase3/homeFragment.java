package com.example.firebase3;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class homeFragment extends Fragment {

    private RecyclerView blog_list_View;

    private List<BlogPost> blog_list;


    private FirebaseFirestore firebaseFirestore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private FirebaseAuth mAuth;

    public DocumentSnapshot lastVisible;

    private boolean firstPageFirstLoad=true;

    private static final String TAG = "homeFragment";


    public homeFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_home, container, false);
            mAuth=FirebaseAuth.getInstance();

        Log.d(TAG, "onCreateView: ");

        blog_list_View=view.findViewById(R.id.homeView);



        blogRecyclerData BlogRecyclerAdapter=new blogRecyclerData(blog_list_View,getContext());

     /*   blog_list=new ArrayList<>();

        blog_list_View=view.findViewById(R.id.homeView);

        blogRecyclerData BlogRecyclerAdapter=new blogRecyclerData(blog_list_View,getContext());

        blog_list_View.setLayoutManager(new LinearLayoutManager(container.getContext()));

        blogRecyclerAdapter=new BlogRecyclerAdapter(blog_list);
        blog_list_View.setAdapter(blogRecyclerAdapter);
        blog_list_View.setHasFixedSize(true);


        firebaseFirestore=FirebaseFirestore.getInstance();

       blog_list_View.addOnScrollListener(new RecyclerView.OnScrollListener() {


            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean reachedBottom=!blog_list_View.canScrollVertically(1);

                if(reachedBottom){

                    Toast.makeText(getActivity(),"scroll up to load more posts",Toast.LENGTH_SHORT).show();
                    loadMorePosts();
                }
            }
        });

        Query firstQuery=firebaseFirestore.collection("Posts").orderBy("timestamp" , Query.Direction.DESCENDING);

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(!queryDocumentSnapshots.isEmpty()) {

                    if (firstPageFirstLoad)
                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                      blog_list.clear();
                       // user_list.clear();

                    }

                    blog_list.clear();




                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {



                        if (doc.getType() == DocumentChange.Type.ADDED) {




                            final String blogPostId = doc.getDocument().getId();

                            final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                            String user_id = doc.getDocument().getString("user_id");
                          firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {

                                        UserDetails userDetail = task.getResult().toObject(UserDetails.class);
                                        Log.d(TAG, firstPageFirstLoad + " mmm ");
                                        if (firstPageFirstLoad) {

                                            user_list.add(userDetail);
                                            blog_list.add(blogPost);
                                            Log.d(TAG, blogPostId + " mmm ");


                                        } else {

                                            blog_list.add(0, blogPost);
                                            user_list.add(0, userDetail);
                                           Log.d(TAG, blogPostId + " mmm ");
                                            Log.d(TAG, firstPageFirstLoad + " mmm ");
                                        }

                                        blogRecyclerAdapter.notifyDataSetChanged();


                                    }

                                }



                            });
                          if(firstPageFirstLoad)
                          {

                              blog_list.add(blogPost);


                          } else {

                              blog_list.add(0, blogPost);

                          }

                            blog_list.add(blogPost);





                        }
                    }// for loop


                    blogRecyclerAdapter.notifyDataSetChanged();


                    firstPageFirstLoad=false;
                }


            }
        });*/




        return  view;
    }






  /*  public void loadMorePosts()
    {

        Query nextQuery=firebaseFirestore.collection("Posts")
                .orderBy("timestamp" , Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

        nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(!queryDocumentSnapshots.isEmpty()) {

                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId=doc.getDocument().getId();
                            final BlogPost blogPost=doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                            //retrieving user details

                           String user_id=doc.getDocument().getString("user_id");
                            firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if(task.getResult().exists())
                                    {
                                        UserDetails userDetail=task.getResult().toObject(UserDetails.class);
                                        user_list.add(userDetail);
                                        blog_list.add(blogPost);

                                        blogRecyclerAdapter.notifyDataSetChanged();

                                    }

                                }

                        });


                            blog_list.add(blogPost);








                        }
                    }

                    blogRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });


    }  */

}
