package com.example.firebase3;

import android.content.Context;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by root on 13/7/18.
 */


public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>  {

    public ArrayList<BlogPost> blog_list;


    public Context context;

    public FirebaseFirestore firebaseFirestore;
    public FirebaseAuth mAuth;







    public BlogRecyclerAdapter(Context context, ArrayList<BlogPost> blog_list)
    {

        this.context = context;
        this.blog_list=blog_list;
       // this.user_list=user_list;

    }



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.single_list_item3,parent,false);


        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();


        return new ViewHolder(view);
    }



    public void onBindViewHolder(final ViewHolder holder, final int position) {

         holder.setIsRecyclable(false);

        final String BlogPostId=blog_list.get(position).BlogPostId;

        final String Current_user_Id=mAuth.getCurrentUser().getUid();

        String desc_text=blog_list.get(position).getDesc();
        holder.setDescription(desc_text);

        final String blog_image=blog_list.get(position).getImageUrl();
        holder.setBlogImg(blog_image);

       long milliSeconds=blog_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(milliSeconds)).toString();
        holder.setDate(dateString);

    //setting on click listener to open new activity when pressed
        
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, BlogPostId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,blogDescription.class);
                intent.putExtra("BLOG_IMAGE", blog_image);
                context.startActivity(intent);


            }
        });

        //user details will be added here

        final String user_id=blog_list.get(position).getUser_id();
       // if((Current_user_Id).equals(user_id))/
       // {
          //  holder.deleteBn.setVisibility(View.VISIBLE);
         //   holder.deleteBn.setEnabled(true);

    //    }

     /*   holder.deleteBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  firebaseFirestore.collection("Posts").document(BlogPostId).delete();
            }
        });*/

                 String Username=blog_list.get(position).getUser_name();
                 String ProfileImage=blog_list.get(position).getUser_image();

                 holder.setUserDetails(ProfileImage,Username);









        //get likes count

//        firebaseFirestore.collection("Posts").document(BlogPostId).collection("likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                int Count=queryDocumentSnapshots.size();
//                holder.blogLikeCount.setText(Count +" likes");
//
//            }
//        });


        //get likes

//        firebaseFirestore.collection("Posts").document(BlogPostId).collection("likes")
//                .document(Current_user_Id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                if(documentSnapshot.exists())
//                {
//                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.after_like_btn));
//
//                }
//
//                else
//
//                {
//
//                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));
//
//                }
//
//            }
//        });
//
//
//        //likes feature
//
//        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                firebaseFirestore.collection("Posts/" + BlogPostId + "/likes").document(Current_user_Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                        if(!task.getResult().exists())
//                        {
//
//                            Map<String, Object> likesMap = new HashMap<>();
//                            likesMap.put("timestamp", FieldValue.serverTimestamp());
//
//                            firebaseFirestore.collection("Posts").document(BlogPostId).collection("likes").document(Current_user_Id).set(likesMap);
//
//                        } else {
//
//                            firebaseFirestore.collection("Posts").document(BlogPostId).collection("likes").document(Current_user_Id).delete();
//                        }
//
//                    }
//                });
//
//
//
//
//            }
//        });
//
//        //Comment feature
//
//        holder.blogCommentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent CommentIntent=new Intent(context,CommentActivity.class);
//                CommentIntent.putExtra("Blog_Id",BlogPostId);
//                context.startActivity(CommentIntent);
//
//            }
//        });
//
    }





    public int getItemCount() {

        return blog_list.size();

    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView descText;
        private ImageView blogImage;
        private TextView dateText;
        private ImageView ProfileImage;
        private TextView UserName;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;
        private ImageView blogCommentButton;
        private Button deleteBn;

        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            blogLikeCount = mView.findViewById(R.id.blogLikeCount);
            blogCommentButton=mView.findViewById(R.id.blogCommentBtn);
            deleteBn=mView.findViewById(R.id.deleteBtn);

            blogLikeBtn=mView.findViewById(R.id.blogLikeBtn);

        }

        public void setDescription(String Text)
        {

            descText=mView.findViewById(R.id.blogText);
            descText.setText(Text);

        }

        public void setBlogImg (String BlogImage)
        {
            blogImage=mView.findViewById(R.id.blogPostImage);
            Glide.with(context).load(BlogImage).into(blogImage);

        }

        public void setDate(String blogDate)
        {

            dateText=mView.findViewById(R.id.blogDate);
            dateText.setText(blogDate);

        }

        public void updateLikesCount(int count){

            blogLikeCount = mView.findViewById(R.id.blogLikeCount);
            blogLikeCount.setText(count + " Likes");

        }

        public void setUserDetails(String profileUrl,String Username)
        {

            ProfileImage=mView.findViewById(R.id.blogProfileImage);
            UserName=mView.findViewById(R.id.blogUsername);

            UserName.setText(Username);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(profileUrl).into(ProfileImage);






        }


    }
}
