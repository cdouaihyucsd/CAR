package com.car.carsquad.carapp;

//import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
public class MessageActivity extends AppCompatActivity {

    private Button add_room;
    private EditText room_name;

    private ListView chatList;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> chatArr = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("chatroom");

    private String driverID;
    private String startPt;
    private String endPt;
    private String myID;
    private String chatRoom;
    //private String room_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //add_room = (Button) findViewById(R.id.btn_add_room);
        chatList = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatArr);
        //room_name = (EditText) findViewById(R.id.room_name);
        chatList.setAdapter(arrayAdapter);
        myID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        /*
        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(room_name.getText().toString(), "");
                root.updateChildren(map);
            }
        });
        */
        //getIncomingIntent();



        root.child(myID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                chatArr.clear();
                chatArr.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),ChatRoomActivity.class);
                //intent.putExtra("room_name",((TextView)view).getText().toString() );

                //TextView chatRoom = (TextView) view.findViewById(R.id.listView);
                String ride = (String) adapterView.getItemAtPosition(i);
                String[] rideArr = ride.split(" - ");
                startPt = rideArr[0];
                endPt = rideArr[1];
                driverID = rideArr[2];
                intent.putExtra("startPt", startPt);
                intent.putExtra("endPt", endPt);
                intent.putExtra("driverID", driverID);
                //intent.putExtra("user_name",Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                startActivity(intent);
            }
        });

    }

    /*
    private void getIncomingIntent(){
        driverID = getIntent().getStringExtra("driverID");
        startPt = getIntent().getStringExtra("startPt");
        endPt = getIntent().getStringExtra("endPt");
    }
    */
}