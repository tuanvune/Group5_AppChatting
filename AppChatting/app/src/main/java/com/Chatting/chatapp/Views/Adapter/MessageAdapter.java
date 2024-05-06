package com.Chatting.chatapp.Views.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Chatting.chatapp.Models.MessageModel;
import com.Chatting.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private ArrayList<MessageModel> listMessage;
    private FirebaseUser mUser;

    public MessageAdapter(Context context, ArrayList<MessageModel> listMessage) {
        this.context = context;
        this.listMessage = listMessage;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.items_row_right_message, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.items_row_left_message, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        MessageModel message = listMessage.get(position);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(message.getSender().equals(mUser.getUid())){
            holder.txtItemMessageRight.setText(message.getMessage());
        } else {
            holder.txtItemMessageLeft.setText(message.getMessage());
            if(message.getImageURL().equals("default")) {
                holder.ciItemMessageLeft.setImageResource(R.drawable.ic_launcher_background);
            }else {
                Picasso.get().load(message.getImageURL()).into(holder.ciItemMessageLeft);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtItemMessageLeft, txtItemMessageRight;
        private CircleImageView ciItemMessageLeft;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemMessageRight = itemView.findViewById(R.id.txtItemMessageRight);
            txtItemMessageLeft = itemView.findViewById(R.id.txtItemMessageLeft);
            ciItemMessageLeft = itemView.findViewById(R.id.ciItemMessageLeft);
        }
    }

    @Override
    public int getItemViewType(int position) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mUser.getUid();
        if(listMessage.get(position).getSender().equals(uid)){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
