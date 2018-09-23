package com.example.firebase3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String current_user_id;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private FloatingActionButton post_btn;

    private BottomNavigationView mainBottomView;

    private homeFragment home_fragment;
    private notificationFragment notification_fragment;
    private accountFragment account_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        post_btn=findViewById(R.id.mainFloatingBtn);

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,NewPostActivity.class);
                startActivity(intent);

            }
        });

        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=firebaseFirestore.getInstance();

        toolbar=findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("buy and sell");

        init();

        replaceFragment(home_fragment);

        mainBottomView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

              switch(item.getItemId())
              {

                  case R.id.bottom_home:

                      replaceFragment(home_fragment);
                      break;

                  case R.id.bottom_notification:

                      replaceFragment(notification_fragment);
                      break;

                  case R.id.bottom_account:

                      replaceFragment(account_fragment);
                      break;



              }
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null)
        {

            sendToLogin();

        } else {

            current_user_id=mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent intent=new Intent(MainActivity.this,setupActivity.class);
                            startActivity(intent);
                            finish();

                        }



                    } else {

                        String error_message=task.getException().getMessage();
                        Toast.makeText(MainActivity.this,error_message,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout_button:

                logout();
                return true;

            case R.id.action_setting_button:

                Intent setup_intent=new Intent(this,setupActivity.class);
                startActivity(setup_intent);
                return true;


            default:
                return false;
        }

    }



    private void logout() {

        mAuth.signOut();
        sendToLogin();
    }



    private void sendToLogin() {

        Intent Loginintent=new Intent(this,loginActivity.class);
        startActivity(Loginintent);
        finish();
    }


    private void replaceFragment(Fragment fragment)
    {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();

    }

    private void init() {

        mainBottomView=findViewById(R.id.MainbottomNavigationView);
        home_fragment=new homeFragment();
        notification_fragment=new notificationFragment();
        account_fragment=new accountFragment();
    }
}
