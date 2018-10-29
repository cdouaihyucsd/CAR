package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void nextActivity(View view) {
        int viewId = view.getId();
        Intent nextActivityIntent;
        switch(viewId) {
            case R.id.login_button:
                nextActivityIntent = new Intent(this, LoginActivity.class);
                startActivity(nextActivityIntent);
                break;
            case R.id.register_button:
                nextActivityIntent = new Intent(this, VerifyActivity.class);
                startActivity(nextActivityIntent);
                break;

            case R.id.forgot_password_button:
                nextActivityIntent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(nextActivityIntent);
                break;
        }
    }


}
