package com.Chatting.chatapp.Presenter.Model;

import android.widget.EditText;

import androidx.viewpager.widget.ViewPager;

public interface IMain {
    void verifyUser();
    void createNewGroup();
    void pushNewGroup(EditText edtGroupName);
    void navigaView(ViewPager vpMain);
}
