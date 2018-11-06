package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.widget.Button;
import android.widget.Toast;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase object
    private FirebaseAuth firebaseAuth;

    //UI references
    private TextView textviewUserEmail;
    private Button buttonLogout;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        //initialize fireBase
        firebaseAuth = firebaseAuth.getInstance();

        //if user is not logged in yet
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //for the sidebar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //display USER EMAIL
        FirebaseUser user = firebaseAuth.getCurrentUser();
        textviewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textviewUserEmail.setText("Welcome "+ user.getEmail());

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
    }

    //for the side bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.nav_logout:
                logout();
                return true;
            default:
                Toast.makeText(this, "* Something Magical Happens *", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        //sign user out
        firebaseAuth.signOut();
        //end current activity and go back to main screen
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onClick(View view) {
       if(view == buttonLogout) {
           logout();
       }
    }

    //prevent user from pressing the back button to go back from the main app screen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
