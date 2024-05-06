package com.Chatting.chatapp.Views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.SearchPresenter;
import com.Chatting.chatapp.Presenter.View.ISearchView;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.Adapter.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements ISearchView {
    private Toolbar tbSearch;
    private EditText edtSearch;
    private RecyclerView rcvSearch;
    private SearchAdapter searchAdapter;
    private ArrayList<UserModel> listUser;
    private UserModel userModel;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private SearchPresenter searchPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initWidget();
        init();
//        getUser();
        searchUser();
    }

    private void searchUser() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchPresenter.processSearch(listUser, charSequence.toString(), searchAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

//    private void getUser() {
//        searchPresenter.getUser(listUser, searchAdapter);
//    }

    private void init() {
        setSupportActionBar(tbSearch);
        getSupportActionBar().setTitle("");
        tbSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        listUser = new ArrayList<>();
        userModel = new UserModel();
        searchPresenter = new SearchPresenter(SearchActivity.this, mAuth, mRef, SearchActivity.this);
        searchAdapter = new SearchAdapter(SearchActivity.this, listUser);
        rcvSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        rcvSearch.setAdapter(searchAdapter);
    }

    private void initWidget() {
        tbSearch = findViewById(R.id.tbSearch);
        edtSearch = findViewById(R.id.edtSearch);
        rcvSearch = findViewById(R.id.rcvSearch);
    }

}
