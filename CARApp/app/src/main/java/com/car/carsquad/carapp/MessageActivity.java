package com.car.carsquad.carapp;

//import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
public class MessageActivity extends AppCompatActivity {

    private EditText room_name;
    private ListView chatList;
    private RecyclerView recyclerView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> chatArr = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("chatroom");
    private DatabaseReference driverData = FirebaseDatabase.getInstance().getReference("users");

    private String driverID;
    private String startPt;
    private String endPt;
    private String myID;
    private String chatRoom;
    private ChatroomAdapter chatroomAdapter;
    private SwipeController swipeController = null;
    private final List<Chatroom> chatRooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chatroomAdapter = new ChatroomAdapter(chatRooms);
        setupRecyclerView();
        setChatroomDataAdapter();



        /*
        chatList = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatArr);
        chatList.setAdapter(arrayAdapter);
        myID = FirebaseAuth.getInstance().getCurrentUser().getUid();


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
        */
    }
    private void setChatroomDataAdapter() {
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(myID)){
                    root.child(myID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot roomName: dataSnapshot.getChildren()){
                                final String roomTitle = roomName.getKey();
                                //Toast.makeText(MessageActivity.this, roomTitle, Toast.LENGTH_SHORT).show();
                                String[] roomArr = roomTitle.split(" - ");
                                final Chatroom room = new Chatroom();

                                driverData.child(roomArr[2]).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        room.setName(dataSnapshot.child("firstName").getValue().toString()
                                        + " " + dataSnapshot.child("lastName").getValue().toString());
                                        room.setProfileImg(dataSnapshot.child("profile_image").getValue());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                /*
                                Toast.makeText(MessageActivity.this, roomTitle, Toast.LENGTH_SHORT).show();
                                Chatroom room = new Chatroom();
                                room.setName(roomTitle);
                                room.setLastMsg("sada");
                                room.setMsgTime("asd");
                                chatRooms.add(room);
                                chatroomAdapter.notifyDataSetChanged();
                                */

                                // String[] rTitle = roomTitle.split(" - ");

                                Query lastMsg = root.child(myID).child(roomTitle).orderByKey().limitToLast(1);

                                lastMsg.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot lastMsg: dataSnapshot.getChildren()){
                                            //Chatroom room = new Chatroom();
                                            //room.setName(lastMsg.child("name").getValue().toString());
                                            room.setMsgTime(lastMsg.child("time").getValue().toString());
                                            room.setLastMsg(lastMsg.child("name").getValue().toString()
                                                    + ": " +lastMsg.child("msg").getValue().toString());
                                            chatRooms.add(room);
                                            chatroomAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        /*
        Chatroom room1 = new Chatroom();
        room1.setName("iugjkhg");
        room1.setLastMsg("testmsg");
        room1.setMsgTime("11:00");
        chatRooms.add(room1);

        Chatroom room2 = new Chatroom();
        room2.setName("asda");
        room2.setLastMsg("asdaa");
        room2.setMsgTime("11:00");
        chatRooms.add(room2);
        chatroomAdapter = new ChatroomAdapter(chatRooms);
        */
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(chatroomAdapter);
        
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                chatroomAdapter.chatRooms.remove(position);
                chatroomAdapter.notifyItemRemoved(position);
                chatroomAdapter.notifyItemRangeChanged(position, chatroomAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

}