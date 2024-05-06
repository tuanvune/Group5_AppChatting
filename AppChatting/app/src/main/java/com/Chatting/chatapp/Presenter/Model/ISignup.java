package com.Chatting.chatapp.Presenter.Model;

import android.widget.EditText;

import com.Chatting.chatapp.Models.UserModel;

public interface ISignup {
    void validUser(UserModel userModel, EditText edtUsername, EditText edtEmail, EditText edtPhone, EditText edtPassword);
    void creatingUser(UserModel userModel);
    void createSuccess(UserModel userModel);
}
