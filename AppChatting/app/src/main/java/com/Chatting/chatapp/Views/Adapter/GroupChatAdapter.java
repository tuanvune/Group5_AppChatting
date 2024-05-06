package com.Chatting.chatapp.Views.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Chatting.chatapp.Models.GroupModel;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.MessageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    private Context context;
    private ArrayList<GroupModel> listGroup;

    public GroupChatAdapter(Context context, ArrayList<GroupModel> listGroup) {
        this.context = context;
        this.listGroup = listGroup;
    }

    @NonNull
    @Override
    public GroupChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_groupchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatAdapter.ViewHolder holder, int position) {
        GroupModel groupModel = listGroup.get(position);
        holder.txtItemGroupChat.setText(groupModel.getName());
        if (groupModel.getImageURL().equals("default")) {
            holder.ciItemGroupChat.setImageResource(R.drawable.ic_launcher_background);
        } else {
            Picasso.get().load(groupModel.getImageURL()).into(holder.ciItemGroupChat);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("message", groupModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView ciItemGroupChat;
        private TextView txtItemGroupChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ciItemGroupChat = itemView.findViewById(R.id.ciItemGroupChat);
            txtItemGroupChat = itemView.findViewById(R.id.txtItemGroupChat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
