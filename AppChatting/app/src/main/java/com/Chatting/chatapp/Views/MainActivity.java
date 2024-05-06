package com.Chatting.chatapp.Views;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.Chatting.chatapp.Presenter.MainPresenter;
import com.Chatting.chatapp.Presenter.View.IMainView;
import com.Chatting.chatapp.Views.Adapter.MainViewPagerAdapter;
import com.Chatting.chatapp.Views.Fragment.Fragment_chat;
import com.Chatting.chatapp.Views.Fragment.Fragment_contact;
import com.Chatting.chatapp.Views.Fragment.Fragment_group;
import com.Chatting.chatapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements IMainView {

    private Toolbar tbMain;
    private TabLayout tabMain;
    public static ViewPager vpMain;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private FloatingActionButton fabtnMain;
    public static TextView text;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        init();
        createGroupChat();
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        mainPresenter = new MainPresenter(MainActivity.this, auth, rootRef, MainActivity.this);
    }

    private void init() {
        setSupportActionBar(tbMain);
        getSupportActionBar().setTitle("Chat App");
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPagerAdapter.AddFragment(new Fragment_chat());
        mainViewPagerAdapter.AddFragment(new Fragment_group());
        mainViewPagerAdapter.AddFragment(new Fragment_contact());
        vpMain.setAdapter(mainViewPagerAdapter);
        tabMain.setupWithViewPager(vpMain);
        tabMain.getTabAt(0).setText("Chat");
        tabMain.getTabAt(1).setText("Group");
        tabMain.getTabAt(2).setText("Contacts");

    }

    private void createGroupChat() {
        fabtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.createNewGroup();
            }
        });
    }

    private void initWidget() {
        tbMain = findViewById(R.id.tbMain);
        tabMain = findViewById(R.id.tabMain);
        vpMain = findViewById(R.id.vpMain);
        fabtnMain = findViewById(R.id.fabtnMain);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.itemLogOut:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                break;
            case R.id.itemEdit:
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
                break;
            case R.id.itemSearch:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            mainPresenter.verifyUser();
        }
    }

    @Override
    public void navigaActivity() {
        mainPresenter.navigaView(vpMain);
    }

    @Override
    public void Message() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        alert.setMessage("Please enter your group name!");
        alert.show();
    }
}