package com.Chatting.chatapp.Presenter.Model;

import android.widget.EditText;
import android.widget.TextView;

import com.Chatting.chatapp.Models.GroupModel;
import com.Chatting.chatapp.Models.MessageModel;
import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Views.Adapter.MessageAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public interface IMessage {
    void getDataView(GroupModel groupModel, TextView txtGroupNameMessage, CircleImageView ciGroupMessage);
    void getDataUser(UserModel userModel);
    void sendMessage(GroupModel groupModel, UserModel userModel, EditText edtMessage);
    void readMessage(ArrayList<MessageModel> listMessage, GroupModel groupModel, MessageAdapter messageAdapter);
}
