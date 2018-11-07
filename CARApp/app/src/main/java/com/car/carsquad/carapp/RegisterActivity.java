package com.car.carsquad.carapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.car.carsquad.carapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static android.Manifest.permission.READ_CONTACTS;

/**
 * A registration screen using email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    // UI references
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private ProgressDialog progressDialog;
    private ImageView bannerImage;

    //firebase authentication object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();

        // Link to UI elements
        progressDialog = new ProgressDialog(this);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonRegister = findViewById(R.id.buttonRegister);
        bannerImage = findViewById(R.id.bannerImage);

        //wait until Register button is clicked
        buttonRegister.setOnClickListener(this);
    }

    //method to register user to Firebase server
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        //empty email check
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_SHORT).show();
            return;
        }
        //empty password check
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        //valid email check
        if(!isEmailValid(email)){
            Toast.makeText(this, "Please enter a valid UCSD Email", Toast.LENGTH_SHORT).show();
            return;
        }
        //valid password check
        if(!isPasswordValid(password)){
            Toast.makeText(this, "Password should contain more than 4 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        //email and password OKAY
        //show a progress bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //create user on Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //close progress bar
                        progressDialog.dismiss();
                        //if creating user on Firebase is successful
                        if(task.isSuccessful()){
                            //send verification link
                            Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {
                                            if(task2.isSuccessful()){
                                                //start LoginActivity
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                                Toast.makeText(getApplicationContext(),
                                                        "Registration successful! Please check your email for verification link",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(RegisterActivity.this,
                                                        Objects.requireNonNull(task2.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        //creating user on Firebase failed
                        else {
                            Toast.makeText(RegisterActivity.this,
                                    Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    //check if email is valid
    private boolean isEmailValid(String email) {
        return email.contains("@ucsd.edu");
    }
    //check is email is valid
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    //when the user clicks register
    @Override
    public void onClick(View view) {
        if(view == buttonRegister) {
            registerUser();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch(view.getId()) {
            case R.id.email :
                hideLogo();
                break;
            case R.id.password :
                hideLogo();
                break;
            case R.id.register_button:
                hideLogo();
                break;
            default:
                showLogo();
        }
    }

    public void hideLogo() {
        bannerImage.setVisibility(View.GONE);
    }

    public void showLogo() {
        bannerImage.setVisibility(View.VISIBLE);
    }
}

