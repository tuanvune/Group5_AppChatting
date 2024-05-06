package com.Chatting.chatapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chatting.chatapp.Models.GroupModel;
import com.Chatting.chatapp.Models.MessageModel;
import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.MessagePresenter;
import com.Chatting.chatapp.Presenter.View.IMessageView;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.Adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity implements IMessageView {
    private GroupModel groupModel;
    private UserModel userModel;
    private Toolbar tbMessage, tbInfo;
    private CircleImageView ciGroupMessage;
    private TextView txtGroupNameMessage;
    private EditText edtWriteMessage;
    private Button btnMessage;
    private RecyclerView rcvMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private MessagePresenter messagePresenter;
    private ArrayList<MessageModel> listMessage;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initWidget();
        init();
        dataIntent();
        getDataView();
        sendMessage();
        readMessage();
    }

    private void readMessage() {
        messagePresenter.readMessage(listMessage, groupModel, messageAdapter);
    }

    private void sendMessage() {
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagePresenter.sendMessage(groupModel, userModel, edtWriteMessage);
            }
        });
    }

    private void getDataView() {
        if (groupModel != null) {
            messagePresenter.getDataView(groupModel, txtGroupNameMessage, ciGroupMessage);
            messagePresenter.getDataUser(userModel);
        }
    }

    private void init() {
        setSupportActionBar(tbMessage);
        getSupportActionBar().setTitle("");
        tbMessage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        messagePresenter = new MessagePresenter(MessageActivity.this, mAuth, mRef, this);
        userModel = new UserModel();
        listMessage = new ArrayList<>();
        messageAdapter = new MessageAdapter(MessageActivity.this, listMessage);
        rcvMessage.setLayoutManager(new LinearLayoutManager(MessageActivity.this, LinearLayoutManager.VERTICAL, false));
        rcvMessage.setAdapter(messageAdapter);
    }

    private void initWidget() {
        tbMessage = findViewById(R.id.tbMessage);
        ciGroupMessage = findViewById(R.id.ciGroupMessage);
        txtGroupNameMessage = findViewById(R.id.txtGroupNameMessage);
        edtWriteMessage = findViewById(R.id.edtWriteMessage);
        btnMessage = findViewById(R.id.btnMessage);
        rcvMessage = findViewById(R.id.rcvMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.itemInfo:

                break;
        }
        return true;
    }

    private void dataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("message")) {
                groupModel = (GroupModel) intent.getSerializableExtra("message");
            }
        }
    }

    @Override
    public void clearMessage() {
        edtWriteMessage.getText().clear();
    }

    @Override
    public void notifyMessage() {
        Toast.makeText(MessageActivity.this, "write somthing", Toast.LENGTH_SHORT).show();
    }
}
