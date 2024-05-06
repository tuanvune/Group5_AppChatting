package com.Chatting.chatapp.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.SignupPresenter;
import com.Chatting.chatapp.Presenter.View.ISignupView;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_signup extends Fragment implements ISignupView {
    private View view;
    private EditText edtUsername, edtEmail, edtPhone, edtPassword;
    private Button btnSingup;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private SignupPresenter signupPresenter;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initWidget();
        singupUser();
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        signupPresenter = new SignupPresenter(getContext(), auth, rootRef, this);
        userModel = new UserModel();
        return view;
    }

    private void singupUser() {
        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    signupPresenter.validUser(userModel, edtUsername, edtEmail, edtPhone, edtPassword);
                }
            }
        });
    }


    private void initWidget() {
        edtUsername = view.findViewById(R.id.edtUsername);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnSingup = view.findViewById(R.id.btnSignup);
    }


    @Override
    public void onSuccess() {
        edtUsername.getText().clear();
        edtEmail.getText().clear();
        edtPhone.getText().clear();
        edtPassword.getText().clear();
        UserActivity.vpUser.setCurrentItem(0, true);
    }

    @Override
    public void onFail() {
    }
}

