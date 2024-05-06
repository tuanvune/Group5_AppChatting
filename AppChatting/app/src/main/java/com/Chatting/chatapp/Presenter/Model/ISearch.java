package com.Chatting.chatapp.Presenter.Model;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Views.Adapter.SearchAdapter;

import java.util.ArrayList;

public interface ISearch {
    void getUser(ArrayList<UserModel> listUser, SearchAdapter adapter);
    void processSearch(ArrayList<UserModel> listUser, String charSequence, SearchAdapter adapter);
}
