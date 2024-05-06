package com.Chatting.chatapp.Presenter.Model;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public interface IEdit {
    void getDataUser(EditText edtUsername, EditText edtStatus, CircleImageView ciProfile);
    void updateDataUser(Activity activity, String name, String status);
    void selectImage(int image_code);
    void uploadImage(int requestCode, int resultCode, Intent intent, int image_pick, CircleImageView ciProfile);
}
