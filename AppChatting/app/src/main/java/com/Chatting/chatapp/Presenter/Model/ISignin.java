package com.Chatting.chatapp.Presenter.Model;

import com.Chatting.chatapp.Models.UserModel;

public interface ISignin {
    void validUserSignin(UserModel userModel);
    void signinUser(UserModel userModel);
}
