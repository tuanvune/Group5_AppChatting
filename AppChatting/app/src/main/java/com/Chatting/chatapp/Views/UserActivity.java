package com.Chatting.chatapp.Views;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.Chatting.chatapp.Views.Adapter.UserViewPagerAdapter;
import com.Chatting.chatapp.Views.Fragment.Fragment_signin;
import com.Chatting.chatapp.Views.Fragment.Fragment_signup;
import com.Chatting.chatapp.R;
import com.google.android.material.tabs.TabLayout;

public class UserActivity extends AppCompatActivity {
    private TabLayout tabUser;
    public static ViewPager vpUser;
    private UserViewPagerAdapter userViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initWidget();
        init();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userViewPagerAdapter = new UserViewPagerAdapter(getSupportFragmentManager());
        userViewPagerAdapter.AddFragment(new Fragment_signin());
        userViewPagerAdapter.AddFragment(new Fragment_signup());
        vpUser.setAdapter(userViewPagerAdapter);
        tabUser.setupWithViewPager(vpUser);
        tabUser.getTabAt(0).setText("SIGNIN");
        tabUser.getTabAt(1).setText("SIGNUP");
    }

    private void initWidget() {
        tabUser = findViewById(R.id.tabUser);
        vpUser = findViewById(R.id.vpUser);
    }

}
