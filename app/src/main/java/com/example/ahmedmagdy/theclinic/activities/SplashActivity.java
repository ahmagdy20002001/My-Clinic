package com.example.ahmedmagdy.theclinic.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.ahmedmagdy.theclinic.Notifications.Token;
import com.example.ahmedmagdy.theclinic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashActivity extends AppCompatActivity {
    ImageView myImageView;
    Animation myAnimation;
    private FirebaseAuth mAuth;
    private FirebaseUser fuser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        //get my image
        myImageView = (ImageView) findViewById(R.id.splash_logo);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!= null){

            updateToken(FirebaseInstanceId.getInstance().getToken());
        }
        // load the animation file (my_anim)
        myAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanime);
        myImageView.startAnimation(myAnimation);

        Thread timer =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);//5000
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                  initAuthStateListener();

/**
                    Intent intent=new Intent(SplashActivity.this,AllDoctorActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();**/
                }
            }
        }) ;
        timer.start();
    }
    private void initAuthStateListener() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    getallData();
                } else {
                    Intent intent=new Intent(SplashActivity.this,AllDoctorActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    // User is signed out
                    // Log.d(TAG, "onAuthStateChanged:signed_out");
                }

    }





    private void getallData() {

        DatabaseReference databaseChat = FirebaseDatabase.getInstance().getReference("ChatRoom");
        //**************************************************//
        // private void getallData();
        final ValueEventListener postListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {

                String usertype = dataSnapshot1.child(mAuth.getCurrentUser().getUid()).child("ctype").getValue(String.class);


                if(usertype .equals("User") ) {
                    Intent iii= new Intent(SplashActivity.this,FavActivity.class);
                    iii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(iii);
                    finish();
                }else if(usertype .equals("Doctor")){
                    Intent iii= new Intent(SplashActivity.this,DoctorBookingsActivity.class);
                    iii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(iii);
                    finish();
                }else{

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        databaseChat .addValueEventListener(postListener1);
    }
    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
}
