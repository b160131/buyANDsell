package com.example.firebase3;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class CommentActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar commentToolbar;

    private RecyclerView comment_list;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String currentUser_id;
    private String blog_post_id;

    private ImageView comment_btn;
    private EditText comment_text;

    private List<CommentList> commentsList;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentToolbar=findViewById(R.id.comment_toolbar);
        setSupportActionBar(commentToolbar);
        getSupportActionBar().setTitle("Comments");

        commentsList=new ArrayList();

        init();

        currentUser_id=mAuth.getCurrentUser().getUid();
        blog_post_id= getIntent().getStringExtra("Blog_Id");

        //recycler setup

        CommentListAdapter commentAdapter=new CommentListAdapter(commentsList);
        comment_list.hasFixedSize();
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentAdapter);



       firebaseFirestore.collection("Posts").document(blog_post_id).collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

               if(!queryDocumentSnapshots.isEmpty())
               {

                   for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                       if (doc.getType() == DocumentChange.Type.ADDED) {

                           CommentList comment=doc.getDocument().toObject(CommentList.class);
                           commentsList.add(comment);



                       }
                   }
               }

           }
       });

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String commentMsg=comment_text.getText().toString();

                HashMap<String,Object> commentMap=new HashMap<>();

                commentMap.put("comment", commentMsg);
                commentMap.put("user_id", currentUser_id);
                commentMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("Posts").document(blog_post_id).collection("Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(task.isSuccessful())
                        {

                            comment_text.setText("");
                            Toast.makeText(CommentActivity.this, "message successfully posted", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(CommentActivity.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                });

            }
        });





    }

    private void init()
    {

        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        comment_text=findViewById(R.id.comment_field);
        comment_btn=findViewById(R.id.comment_post_btn);
        comment_list=findViewById(R.id.comment_list);

    }


}
