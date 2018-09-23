package com.example.firebase3;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registrationActivity extends AppCompatActivity {

    private EditText regEmailText;
    private EditText regPassText;
    private EditText regConfirmPassText;
    private Button regButton;
    private Button regLoginButton;

    private ProgressBar regProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();

        init();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=regEmailText.getText().toString();
                String pass=regPassText.getText().toString();
                String ConfirmPass=regConfirmPassText.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(ConfirmPass))
                {

                    if(pass.equals(ConfirmPass)){

                        regProgressBar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {

                                    sendTosetupActivity();

                                } else {

                                    String errorMessage=task.getException().getMessage();
                                    Toast.makeText(registrationActivity.this,"Error : " + errorMessage,Toast.LENGTH_SHORT).show();

                                }

                                regProgressBar.setVisibility(View.INVISIBLE);

                            }
                        });

                    } else {

                        Toast.makeText(registrationActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        regLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendtoLogin();
            }
        });
    }




    private void init() {

        regEmailText=findViewById(R.id.regEmail);
        regPassText=findViewById(R.id.regPass);
        regConfirmPassText=findViewById(R.id.regConfirmPass);
        regButton=findViewById(R.id.regButton);
        regLoginButton=findViewById(R.id.regLoginButton);

        regProgressBar=findViewById(R.id.reg_progress);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser!=null)
            
            sendToMain();
        
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(registrationActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }


    private void sendtoLogin() {

        Intent mainIntent = new Intent(registrationActivity.this, loginActivity.class);
        startActivity(mainIntent);
        finish();

    }

    private void sendTosetupActivity() {

        Intent mainIntent = new Intent(registrationActivity.this, setupActivity.class);
        startActivity(mainIntent);
        finish();

    }


}
