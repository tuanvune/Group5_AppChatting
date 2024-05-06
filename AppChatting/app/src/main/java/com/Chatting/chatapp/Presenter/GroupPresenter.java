package com.Chatting.chatapp.Presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.Chatting.chatapp.Models.GroupModel;
import com.Chatting.chatapp.Presenter.Model.IGroup;
import com.Chatting.chatapp.Views.Adapter.GroupChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupPresenter implements IGroup {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    public GroupPresenter(Context context, FirebaseAuth mAuth, DatabaseReference mRef) {
        this.context = context;
        this.mAuth = mAuth;
        this.mRef = mRef;
    }

    @Override
    public void getDataGroup(ArrayList<GroupModel> listGroup, GroupChatAdapter groupChatAdapter) {
        mRef.child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGroup.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    GroupModel groupModel = snap.getValue(GroupModel.class);
                    groupModel.setName(snap.getKey());
                    listGroup.add(groupModel);
                }
                groupChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }
}
