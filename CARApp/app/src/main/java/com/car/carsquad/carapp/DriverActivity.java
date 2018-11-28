package com.car.carsquad.carapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
//import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import android.support.v7.widget.SearchView;

import java.util.HashMap;
import java.util.Objects;

public class DriverActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mAddPost;
    private FloatingActionButton mDeletePost;

    private RecyclerView mPostList;
    private DatabaseReference mDatabase;
    //Firebase object
    private FirebaseAuth firebaseAuth;

    private String start;
    private String destination;

    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        firebaseAuth = firebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabase.keepSynced(true);
        mPostList = (RecyclerView) findViewById(R.id.driver_post_view);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));

        mAddPost = (FloatingActionButton) findViewById(R.id.add_post);
        mAddPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent nextActivityIntent = new Intent(DriverActivity.this, DriverPostActivity.class);
                //finish();
                startActivity(nextActivityIntent);
            }
        });

        //for the sidebar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout2);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            //logout from menu bar
                            case R.id.nav_logout:
                                logout();
                                break;
                            case R.id.nav_switch_to_rider:
                                AlertDialog.Builder builder = new AlertDialog.Builder(DriverActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("You are about to enter RIDER mode");
                                builder.setMessage("Do you wish to proceed?");

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("currentMode", "rider");
                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                .child(userId).updateChildren(result);
                                        startActivity(new Intent(DriverActivity.this, RiderActivity.class));
                                    }
                                });
                                builder.show();
                                break;
                            case R.id.nav_account:
                                startActivity(new Intent(DriverActivity.this, UpdateUserInfoActivity.class));
                        }
                        return true;
                    }
                });
    }

    //search for posts
   /* private void firebaseSearch(String searchText){
        Query firebaseSearchQuery = mDatabase.orderByChild("startPt")
                .startAt(searchText).endAt(searchText + "/uf8ff");
        FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post, PostViewHolder>
                        (Post.class, R.layout.post_cardview, PostViewHolder.class, firebaseSearchQuery){
                    @Override
                    protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position){
                        viewHolder.setTitle(model.getStartPt());
                        viewHolder.setDesc(model.getEndPt());
                        viewHolder.setDepDate(model.getDate());
                        viewHolder.setDepTime(model.getTime());
                    }
                };
        mPostList.setAdapter(firebaseRecyclerAdapter);
    }*/

    @Override
    protected void onStart(){
        super.onStart();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Query firebaseSearchQuery = mDatabase.orderByChild("userID").equalTo(userId);

        FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post, PostViewHolder>
                        (Post.class, R.layout.post_cardview, PostViewHolder.class, firebaseSearchQuery/*mDatabase*/){
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position){
                viewHolder.setStart(model.getStartPt().toUpperCase());
                viewHolder.setDest(model.getEndPt().toUpperCase());
                viewHolder.setDate(model.getDate());
                //viewHolder.setTime(model.getTime());
                viewHolder.setCost(model.getCost());
                viewHolder.setDetours("NULL");
                viewHolder.setTime(model.getTime());
            }
        };
        mPostList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /*getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PostViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DriverPostDetails.class);
                    context.startActivity(intent);
                }
            });
        }
        public void setStart(String start){
            TextView post_start = (TextView)mView.findViewById(R.id.post_start);
            post_start.setText(start);
        }
        public void setDest(String dest){
            TextView post_dest = (TextView)mView.findViewById(R.id.post_dest);
            post_dest.setText(dest);
        }
        public void setDate(String depDate){
            TextView post_date = (TextView)mView.findViewById(R.id.post_date);
            post_date.setText("DATE: " +depDate);
        }
        public void setCost(String cost){
            TextView post_dep_date = (TextView)mView.findViewById(R.id.post_cost);
            post_dep_date.setText("$" + cost);
        }
        public void setDetours(String detours){
            TextView post_detours = (TextView)mView.findViewById(R.id.post_detours);
            post_detours.setText(detours + " stops along the way");
        }
        public void setTime(String depTime){
            TextView post_dep_time = (TextView)mView.findViewById(R.id.post_time);
            post_dep_time.setText("TIME: " + depTime);
        }

    }

    //prevent user from pressing the back button to go back from the main app screen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //LOGOUT of the app
    private void logout() {
        //sign user out
        firebaseAuth.signOut();
        //end current activity and go back to main screen
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
