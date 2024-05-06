package com.Chatting.chatapp.Presenter;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.Chatting.chatapp.Models.GroupModel;
import com.Chatting.chatapp.Models.MessageModel;
import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.Model.IMessage;
import com.Chatting.chatapp.Presenter.View.IMessageView;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.Adapter.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagePresenter implements IMessage {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private IMessageView callback;
    private String currentUserId = "";

    public MessagePresenter(Context context, FirebaseAuth mAuth, DatabaseReference mRef, IMessageView callback) {
        this.context = context;
        this.mAuth = mAuth;
        this.mRef = mRef;
        this.callback = callback;
    }

    @Override
    public void getDataView(GroupModel groupModel, TextView txtGroupNameMessage, CircleImageView ciGroupMessage) {
        if (groupModel != null) {
            txtGroupNameMessage.setText(groupModel.getName());
            if (groupModel.getImageURL().equals("default")) {
                ciGroupMessage.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Picasso.get().load(groupModel.getImageURL()).into(ciGroupMessage);
            }

        }
    }

    @Override
    public void getDataUser(UserModel userModel) {
        currentUserId = mAuth.getCurrentUser().getUid();
        mRef.child("users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel_ = userModel;
                userModel_ = snapshot.getValue(UserModel.class);
                String img = snapshot.child("imageURL").getValue().toString();
                userModel.setImageURL(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }

        });
    }


    @Override
    public void sendMessage(GroupModel groupModel, UserModel userModel, EditText edtMessage) {
        getDataUser(userModel);
        String image = userModel.getImageURL();
        String message = edtMessage.getText().toString().trim();
        String sender = mAuth.getCurrentUser().getUid();
        String nameGroup = groupModel.getName();
        HashMap<String, Object> map = new HashMap<>();
        map.put("sender", sender);
        map.put("message", message);
        map.put("imageURL", image);
        mRef.child("groups").child(nameGroup).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.clearMessage();
                } else {
                    callback.notifyMessage();
                }
            }
        });
    }

    @Override
    public void readMessage(ArrayList<MessageModel> listMessage, GroupModel groupModel, MessageAdapter messageAdapter) {
        ArrayList<MessageModel> finalListMessage = listMessage;
        String uid = mAuth.getCurrentUser().getUid();
        String groupName = groupModel.getName();
        mRef.child("groups").child(groupName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finalListMessage.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals("imageURL")) {
                        MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                        String sender = dataSnapshot.child("sender").getValue().toString();
                        String message = dataSnapshot.child("message").getValue().toString();
                        String img = dataSnapshot.child("imageURL").getValue().toString();
                        messageModel.setSender(sender);
                        if (messageModel.getSender().equals(uid)) {
                            messageModel.setMessage(message);
                        } else {
                            messageModel.setMessage(message);
                            messageModel.setImageURL(img);
                        }
                        finalListMessage.add(messageModel);
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }


}
