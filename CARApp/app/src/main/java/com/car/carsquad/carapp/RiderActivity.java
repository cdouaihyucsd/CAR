package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RiderActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase object
    private FirebaseAuth firebaseAuth;

    //UI references
    private TextView textviewUserEmail;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

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
        //FirebaseUser user = firebaseAuth.getCurrentUser();
        //textviewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        //textviewUserEmail.setText("Welcome "+ user.getEmail());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        // menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch (menuItem.getItemId()) {
                            //logout from menu bar
                            case R.id.nav_logout:
                                logout();
                                break;
                            case R.id.nav_switch_to_driver:
                                startActivity(new Intent(RiderActivity.this, DriverProfileActivity.class));
                                break;
                            case R.id.nav_account:
                                startActivity(new Intent(RiderActivity.this, UpdateUserInfoActivity.class));
                        }
                        return true;
                    }
                });
    }

    //for the side bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    //LOGOUT of the app
    private void logout() {
        //sign user out
        firebaseAuth.signOut();
        //end current activity and go back to main screen
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onClick(View view) {
    }

    //prevent user from pressing the back button to go back from the main app screen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
