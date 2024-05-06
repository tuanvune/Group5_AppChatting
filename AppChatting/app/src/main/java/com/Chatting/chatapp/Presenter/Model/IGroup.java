package com.Chatting.chatapp.Presenter.Model;

import com.Chatting.chatapp.Models.GroupModel;
import com.Chatting.chatapp.Views.Adapter.GroupChatAdapter;

import java.util.ArrayList;

public interface IGroup {
    void getDataGroup(ArrayList<GroupModel> listGroup, GroupChatAdapter groupChatAdapter);
}
