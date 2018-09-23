package com.example.firebase3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {

    private Toolbar post_toolbar;
    private ImageView post_Image;
    private EditText post_desk;
    private Button post_button;

    private ProgressBar post_progress;

    private Uri postImageUri;

    private String user_id;

    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore userFirestore;

    private Bitmap compressedImageFile;
    private Uri thumbDownloadUri;
    private String currentUserId,UserName,UserImage;


    private static final String TAG = "NewPostActivity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        post_toolbar=findViewById(R.id.NewPostToolbar);


        init();

        setSupportActionBar(post_toolbar);
        getSupportActionBar().setTitle("Add New Post");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        currentUserId=mAuth.getCurrentUser().getUid();

        //retrieving user details

        userFirestore.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists()) {

                        UserName = task.getResult().getString("name");
                        UserImage = task.getResult().getString("image");

                    }
                }
            }
        });



        post_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewPostActivity.this);
            }
        });


        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final  String post_desc=post_desk.getText().toString();

                if(!TextUtils.isEmpty(post_desc)&&postImageUri!=null){

                    post_progress.setVisibility(View.VISIBLE);



                    final String randomName=  UUID.randomUUID().toString();

                    final StorageReference FilePath=storageReference.child("post_images").child(randomName + ".jpg");

                    UploadTask uploadTask = FilePath.putFile(postImageUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return FilePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {

                                File newThumbFile = new File(postImageUri.getPath());

                                try {

                                    compressedImageFile=new Compressor(NewPostActivity.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(1)
                                            .compressToBitmap(newThumbFile);

                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }


                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                final byte[] thumbData = baos.toByteArray();



                                final UploadTask uploadTask = storageReference.child("post_images/thumbs")
                                        .child(randomName + ".jpg").putBytes(thumbData);

                             /*  final StorageReference ref=storageReference.child("post_images/thumbs").child(randomName+ ".jpg");


                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        // Continue with the task to get the download URL
                                        return ref.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            thumbDownloadUri = task.getResult();
                                        } else {
                                            // Handle failures
                                            // ...
                                        }
                                    }
                                });*/

                              /*  uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                         storageReference.child("post_images/thumbs").child(randomName+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        thumbDownloadUri=uri;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        String errorMessage=exception.getMessage().toString();
                                        Toast.makeText(NewPostActivity.this,errorMessage,Toast.LENGTH_LONG).show();
                                        // Handle any errors
                                    }
                                });

                                    }
                                });*/





                                Uri download_uri = task.getResult();

                                Map<String,Object> postMap=new HashMap<>();




                                postMap.put("imageUrl" , download_uri.toString());
                                postMap.put("desc" , post_desc);
                                postMap.put("user_id" , currentUserId);
                                postMap.put("timestamp", FieldValue.serverTimestamp());
                                postMap.put("username" , UserName);
                                postMap.put("userimage" , UserImage);
                               // postMap.put("ThumbImageUrl" , thumbDownloadUri.toString());




                                Log.d(TAG, "onComplete: ");

                                firebaseFirestore.collection("Posts").document(randomName).set(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {



                                        if(task.isSuccessful()){

                                            Toast.makeText(NewPostActivity.this,"Blog Post Is Successfully Added",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(NewPostActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {

                                            String error_message=task.getException().getMessage();
                                            Toast.makeText(NewPostActivity.this,error_message,Toast.LENGTH_LONG).show();


                                        }

                                        post_progress.setVisibility(View.INVISIBLE);

                                    }
                                });

                            } else {

                                String error_message=task.getException().getMessage();
                                Toast.makeText(NewPostActivity.this,error_message,Toast.LENGTH_LONG).show();

                            }
                        }

                });




                } else {

                    Toast.makeText(NewPostActivity.this,"field is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {

        post_Image=findViewById(R.id.postImage);
        post_desk=findViewById(R.id.post_text);
        post_button=findViewById(R.id.postButton);
        post_progress=findViewById(R.id.postProgress);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userFirestore=FirebaseFirestore.getInstance();





    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();
                post_Image.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }





}


