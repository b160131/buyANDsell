package com.example.firebase3;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    public List<CommentList> commentsList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    public CommentListAdapter(List<CommentList> commentsList) {
        this.commentsList = commentsList;

    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        firebaseFirestore=FirebaseFirestore.getInstance();


        String CommentMessage=commentsList.get(position).getComment();
        holder.setComment(CommentMessage);

        String user_id=commentsList.get(position).getUser_id();
        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {

                    String Username=task.getResult().getString("name");
                    String ProfileImage=task.getResult().getString("image");

                    holder.setUserDetails(ProfileImage,Username);

                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView cmtText;
        private ImageView ProfileImage;
        private TextView UserName;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setComment(String Msg) {

            cmtText = mView.findViewById(R.id.CommentMessage);
            cmtText.setText(Msg);
        }

        public void setUserDetails(String profileUrl, String Username) {

            ProfileImage = mView.findViewById(R.id.CommentProfileImage);
            UserName = mView.findViewById(R.id.CommentUserName);

            UserName.setText(Username);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(profileUrl).into(ProfileImage);

        }
    }

}
