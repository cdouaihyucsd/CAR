package com.car.carsquad.carapp;


import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ChatRoomActivity extends AppCompatActivity {
    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;
    private ScrollView mScrollView;

    private String user_name,room_name;
    private DatabaseReference root ;
    private DatabaseReference root2;
    private String temp_key;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseUser;

    private String userId;
    private String myID;
    private String driverID;
    private String startPt;
    private String endPt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btn_send_msg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = Objects.requireNonNull(user).getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference("users");

        // Get First Name and Last Name for the current User
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference fN = ref.child(userId).child("firstName");


        fN.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference lN = ref.child(userId).child("lastName");

        lN.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name += " " + dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //room_name = getIntent().getExtras().get("room_name").toString();
        //setTitle(" Room - "+room_name);
        getIncomingIntent();
        myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setTitle(startPt.toUpperCase() + " - " + endPt.toUpperCase());

        /*
        root = FirebaseDatabase.getInstance().getReference("chatroom").child(startPt.toUpperCase()
                + " - " + endPt.toUpperCase());
        */
        root = FirebaseDatabase.getInstance().getReference("chatroom").child(myID)
                .child(startPt.toUpperCase() + " - " + endPt.toUpperCase()+" - "+driverID);
        root2 = FirebaseDatabase.getInstance().getReference("chatroom").child(driverID)
                .child(startPt.toUpperCase() + " - " + endPt.toUpperCase()+ " - "+myID);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("users").child(driverID).child("fcmToken")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String recipientToken = dataSnapshot.getValue(String.class);
                        Toast.makeText(ChatRoomActivity.this, recipientToken, Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                if(input_msg.length() > 0) {
                    FirebaseDatabase.getInstance().getReference().child("users").child(driverID).child("fcmToken")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String  sender = user_name;
                                    String  token = dataSnapshot.getValue(String.class);
                                    String  message_text = input_msg.getText().toString();
                                    Message message = new Message(sender, token, message_text);
                                    Message.sendMessage(message, ChatRoomActivity.this);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                    Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = root.push().getKey();
                    root.updateChildren(map);
                    root2.updateChildren(map);
                    DatabaseReference message_root = root.child(temp_key);
                    DatabaseReference message_root2 = root2.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("name", user_name);
                    map2.put("msg", input_msg.getText().toString());
                    input_msg.setText("");
                    message_root.updateChildren(map2);
                    message_root2.updateChildren(map2);
                }
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chat_msg,chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            Toast.makeText(ChatRoomActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
            chat_conversation.append(Html.fromHtml("<font><b><u>" + chat_user_name + ":</u></b></font> " + chat_msg + " <br>"));
            chat_conversation.invalidate();
            mScrollView.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
        }

        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                 mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    private void getIncomingIntent(){
        driverID = getIntent().getStringExtra("driverID");
        startPt = getIntent().getStringExtra("startPt");
        endPt = getIntent().getStringExtra("endPt");
    }
}
