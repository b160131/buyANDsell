package com.example.firebase3;

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

public class loginActivity extends AppCompatActivity {

    private EditText LoginEmailText;
    private EditText LoginPassText;
    private Button LoginBtn;
    private Button LoginRegBtn;

    private FirebaseAuth mAuth;

    private ProgressBar loginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        LoginEmailText=findViewById(R.id.reg_email);
        LoginPassText=findViewById(R.id.reg_confirm_pass);
        LoginBtn=findViewById(R.id.login_btn);
        LoginRegBtn=findViewById(R.id.login_reg_btn);

        loginProgress=findViewById(R.id.login_progress);


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginEmail=LoginEmailText.getText().toString();
                String loginPass=LoginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail)&&!TextUtils.isEmpty(loginPass))
                {

                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword( loginEmail, loginPass ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                sendToMain();

                            } else {


                                String errorMessage=task.getException().getMessage();
                                Toast.makeText(loginActivity.this,"Error : " + errorMessage ,Toast.LENGTH_SHORT).show();
                            }

                            loginProgress.setVisibility(View.INVISIBLE);

                        }
                    });


                }
            }
        });


        LoginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainIntent=new Intent(loginActivity.this,registrationActivity.class);
                startActivity(mainIntent);
                finish();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {

           sendToMain();

        }
    }

    private void sendToMain()
    {

        Intent mainIntent=new Intent(this,MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
