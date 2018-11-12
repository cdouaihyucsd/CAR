package com.car.carsquad.carapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // UI references
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonForgotPassword;
    private ProgressDialog progressDialog;

    //firebase authentication object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if User is already logged in, skip this login activity
        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            //start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), RiderActivity.class));
        }

        // Link to UI elements
        progressDialog = new ProgressDialog(this);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonForgotPassword = findViewById(R.id.forgot_password_button);

        //wait until Register button is clicked
        buttonLogin.setOnClickListener(this);
        buttonForgotPassword.setOnClickListener(this);
    }

    //method to register user to Firebase server
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //empty email check
        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }
        //empty password check
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }
        //valid email check
        if(!isEmailValid(email)){
            //email is invalid
            Toast.makeText(this, "Please enter a valid UCSD Email",
                    Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }
        //valid password check
        if(!isPasswordValid(password)){
            //password is invalid
            Toast.makeText(this, "Password should contain more than 4 characters",
                    Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }

        //email and password OKAY
        //show a progress bar
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        //allow user to login with email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                //go to main app screen if sign in successfully
                                startActivity(new Intent(getApplicationContext(), RiderActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Please verify your email using the verification link", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        if(view == buttonLogin) {
            userLogin();
        } else if (view == buttonForgotPassword) {
            finish();
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        }
    }
}

