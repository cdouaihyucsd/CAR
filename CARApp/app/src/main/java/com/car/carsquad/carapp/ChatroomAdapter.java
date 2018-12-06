package com.car.carsquad.carapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ChatroomViewHolder> {

    public List<Chatroom> chatRooms;

    public class ChatroomViewHolder extends RecyclerView.ViewHolder {
        private TextView name, msgTime, lastMsg;
        private CircleImageView profileImg;

        public ChatroomViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            msgTime = (TextView) view.findViewById(R.id.msgTime);
            lastMsg = (TextView) view.findViewById(R.id.lastMsg);
            profileImg = (CircleImageView) view.findViewById(R.id.profile_image);
        }
    }

    public ChatroomAdapter(List<Chatroom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    @Override
    public ChatroomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_row, parent, false);

        //itemView.setOnClickListener(new V);

        return new ChatroomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatroomViewHolder holder, int position) {
        Chatroom chatR = chatRooms.get(position);
        holder.name.setText(chatR.getName());
        holder.msgTime.setText(chatR.getMsgTime());
        holder.lastMsg.setText(chatR.getLastMsg());

        // Get profile image
        if (chatR.getProfileImg() != null) {
            String image = chatR.getProfileImg().toString();
            if (image != null)
                Picasso.get().load(image).placeholder(R.drawable.profile).into(holder.profileImg);
            else
                holder.profileImg.setImageResource(R.drawable.profile);
        } else
            holder.profileImg.setImageResource(R.drawable.profile);



    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

}
