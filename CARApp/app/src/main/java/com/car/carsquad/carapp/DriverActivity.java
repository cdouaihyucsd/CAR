package com.car.carsquad.carapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DriverActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mAddPost;
    private Button backToRider;

    private RecyclerView mPostList;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabase.keepSynced(true);
        mPostList = (RecyclerView) findViewById(R.id.driver_post_view);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));

        mAddPost = (FloatingActionButton) findViewById(R.id.add_post);
        backToRider = (Button) findViewById(R.id.back_to_rider);

        mAddPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent nextActivityIntent = new Intent(DriverActivity.this, DriverPostActivity.class);
                finish();
                startActivity(nextActivityIntent);
            }
        });

        backToRider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent nextActivityIntent = new Intent(DriverActivity.this, RiderActivity.class);
                finish();
                startActivity(nextActivityIntent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post, PostViewHolder>
                        (Post.class, R.layout.post_cardview, PostViewHolder.class, mDatabase){
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position){
                viewHolder.setTitle(model.getStartPt());
                viewHolder.setDesc(model.getEndPt());

            }
        };
        mPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PostViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
        public void setImage(Context ctx, String image){
            ImageView postImage = (ImageView)mView.findViewById(R.id.post_image);
            //Picasso.with(ctx).load(image).into(postImage);
        }
    }

    //prevent user from pressing the back button to go back from the main app screen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
