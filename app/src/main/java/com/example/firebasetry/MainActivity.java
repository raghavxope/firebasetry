package com.example.firebasetry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String mUsername;

    int RC_SIGN_IN = 1;
    int RESULT_OK=1;

    private FirebaseAuth mAuth;
    String musername;
    TextView tvm;
    EditText edm;
    Button btm;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListner;
    private FirebaseStorage mFirebaseStorage;

    private FirebaseAuth.AuthStateListener mAuthStateListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mFirebaseReference=FirebaseDatabase.getInstance().getReference().child("message");

        btm=(Button) findViewById(R.id.bt);
        tvm=(TextView) findViewById(R.id.tv);
        edm= findViewById(R.id.ed);
        mAuth = FirebaseAuth.getInstance();
        btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=edm.getText().toString().trim();
                Firebaseuser user =new Firebaseuser(musername,text);
                mFirebaseReference.push().setValue(user);
            }
        });
        Toast.makeText(this,"iugfdfghjkl",Toast.LENGTH_LONG).show();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user signed-in
                    OnSignedIn(user.getDisplayName());
                    Toast.makeText(MainActivity.this, "You are signed.", Toast.LENGTH_SHORT).show();
                } else {
                    //user signed-out
                    OnSignedOut();
                   // Toast.makeText(MainActivity.this,"the parameter is sending",Toast.LENGTH_LONG).show();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(MainActivity.this, "before if condition", Toast.LENGTH_LONG).show();
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed-In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign-In Cancelled", Toast.LENGTH_SHORT).show();
//                finish();
            }


        }
    }


    protected void OnSignedIn(String username) {
        musername = username;
        // attachDatabaseReadListener();
    }

    protected void OnSignedOut() {
        mUsername = "Anonymous";
       // mMessageAdapter.clear();
       // detachDatabaseReadListener();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener !=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

}