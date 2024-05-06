package com.Chatting.chatapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Chatting.chatapp.R;

public class IntroActivity extends AppCompatActivity {

    private static final int TIME_PICKER = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(IntroActivity.this, UserActivity.class));
            }
        }, TIME_PICKER);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
