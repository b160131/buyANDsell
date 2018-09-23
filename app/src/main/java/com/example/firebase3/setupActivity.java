package com.example.firebase3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class setupActivity extends AppCompatActivity {

    private Toolbar setup_toolbar;
    private CircleImageView setup_Image;
    private Button setup_button;
    private EditText setup_name;
    private ProgressBar setup_progress;

    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged=false;

    private static final String TAG = "setup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);



        setup_Image = findViewById(R.id.setupImage);
        setup_name = findViewById(R.id.setupName);
        setup_button = findViewById(R.id.setupButton);
        setup_progress = findViewById(R.id.setup_progress);

        setup_toolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setup_toolbar);
        getSupportActionBar().setTitle("Account Setup");

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user_id=mAuth.getCurrentUser().getUid();

        setup_progress.setVisibility(View.VISIBLE);
        setup_button.setEnabled(false);

        //whenever we open this activity and the user details is available than the details is uploaded otherwise not


        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                    String name=task.getResult().getString("name");
                    String image=task.getResult().getString("image");



                    setup_name.setText(name);
                    mainImageURI=Uri.parse(image);


                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_image);

                        Glide.with(setupActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setup_Image);
                    }



                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(setupActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

                setup_progress.setVisibility(View.INVISIBLE);
                setup_button.setEnabled(true);
            }
        });


        setup_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(setupActivity.this, "permission granted", Toast.LENGTH_SHORT).show();

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)

                        .start(setupActivity.this);

            }
        });

        setup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user_name=setup_name.getText().toString();


                if(!TextUtils.isEmpty(user_name)&&mainImageURI!=null)
                {
                    setup_progress.setVisibility(View.VISIBLE);

                    if(isChanged) {


                        final StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");
                        UploadTask uploadTask = image_path.putFile(mainImageURI);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return image_path.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            public void onComplete(@NonNull Task<Uri> task) {


                                if (task.isSuccessful()) {

                                    storeFirestore(user_name, task);


                                } else {

                                    String Error_message = task.getException().getMessage();
                                    Toast.makeText(setupActivity.this, Error_message, Toast.LENGTH_LONG).show();

                                }


                            }
                        });


                    } else {

                                storeFirestore(user_name,null);
                          }

                     setup_progress.setVisibility(View.INVISIBLE);


                } else {

                    Toast.makeText(setupActivity.this,"Field Is Empty",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void storeFirestore(String user_name,Task<Uri> task) {

        Uri download_uri;

        //if user update the image than task will not will be null

        if(task==null) {

            //as the user has not updated the image so the present uri will be that of previously uploaded image

            download_uri=mainImageURI;

        } else {

             download_uri = task.getResult();
        }


        Map<String,String>  userMap=new HashMap<>();
        userMap.put("name",user_name);
        userMap.put("image",download_uri.toString());

        firebaseFirestore.collection("users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {

            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Toast.makeText(setupActivity.this,"the user settings are updated",Toast.LENGTH_LONG).show();
                    Intent mainIntent=new Intent(setupActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(setupActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }
                setup_progress.setVisibility(View.INVISIBLE);


            }
        });
    }

    //this method is called after cropping process

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setup_Image.setImageURI(mainImageURI);
                isChanged=true;


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}
