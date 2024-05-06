package com.Chatting.chatapp.Presenter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.Model.ISearch;
import com.Chatting.chatapp.Presenter.View.ISearchView;
import com.Chatting.chatapp.Views.Adapter.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchPresenter implements ISearch {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private ISearchView callback;

    public SearchPresenter(Context context, FirebaseAuth mAuth, DatabaseReference mRef, ISearchView callback) {
        this.context = context;
        this.mAuth = mAuth;
        this.mRef = mRef;
        this.callback = callback;
    }

    @Override
    public void getUser(ArrayList<UserModel> listUser, SearchAdapter adapter) {
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserModel userModel = data.getValue(UserModel.class);
                    listUser.add(userModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("BUG", error.getMessage());
            }
        });
    }

    public void getUsers(ArrayList<UserModel> listUser, SearchAdapter adapter) {
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserModel userModel = data.getValue(UserModel.class);
                    listUser.add(userModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("BUG", error.getMessage());
            }
        });
    }

    @Override
    public void processSearch(ArrayList<UserModel> listUser, String charSequence, SearchAdapter adapter) {
        getUsers(listUser, adapter);
        Query query = mRef.child("users").orderByChild("username").startAt(charSequence).endAt(charSequence + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserModel userModel = data.getValue(UserModel.class);
                    listUser.add(userModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
