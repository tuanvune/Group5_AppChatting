package com.Chatting.chatapp.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chatting.chatapp.Models.GroupModel;
import com.Chatting.chatapp.Presenter.GroupPresenter;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.Adapter.GroupChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Fragment_group extends Fragment {
    private View view;
    private RecyclerView rcvGroupChat;
    private GroupChatAdapter groupChatAdapter;
    private FirebaseAuth auth;
    private DatabaseReference groupRef;
    private ArrayList<GroupModel> listGroup = new ArrayList<>();
    private GroupPresenter groupPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        initWidget();
        init();
        getDataGroup();
        return view;
    }

    private void getDataGroup() {
        groupPresenter.getDataGroup(listGroup, groupChatAdapter);
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        groupRef = FirebaseDatabase.getInstance().getReference();
        groupPresenter = new GroupPresenter(getContext(), auth, groupRef);
        groupChatAdapter = new GroupChatAdapter(getContext(), listGroup);
        rcvGroupChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvGroupChat.setAdapter(groupChatAdapter);
    }

    private void initWidget() {
        rcvGroupChat = view.findViewById(R.id.rcvGroup);
    }
}
