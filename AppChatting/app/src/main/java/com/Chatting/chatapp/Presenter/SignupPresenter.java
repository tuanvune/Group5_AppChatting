package com.Chatting.chatapp.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.Model.ISignup;
import com.Chatting.chatapp.Presenter.View.ISignupView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class SignupPresenter implements ISignup {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private String currentUserId = "", status = "";
    private View view;
    private ISignupView callback;
    private ProgressDialog progressDialog;

    public SignupPresenter(Context context, FirebaseAuth mAuth, DatabaseReference mRef, ISignupView callback) {
        this.context = context;
        this.mAuth = mAuth;
        this.mRef = mRef;
        this.callback = callback;
    }

    @Override
    public void validUser(UserModel userModel, EditText edtUsername, EditText edtEmail, EditText edtPhone, EditText edtPassword) {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Username is required!");
            edtUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required!");
            edtEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email is not valid!");
            edtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone is required!");
            edtPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required!");
            edtPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            edtPassword.setError("Password should be length 6 character!");
            edtPassword.requestFocus();
            return;
        }
        userModel.setUsername(username);
        userModel.setEmail(email);
        userModel.setPhone(phone);
        userModel.setPassword(password);
        creatingUser(userModel);
    }

    @Override
    public void creatingUser(UserModel userModel) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Creating New Account");
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(userModel.getEmail(), userModel.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createSuccess(userModel);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BUG", e.getMessage() + "");
            }
        });
    }

    @Override
    public void createSuccess(UserModel userModel) {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", currentUserId);
        map.put("username", userModel.getUsername());
        map.put("email", userModel.getEmail());
        map.put("phone", userModel.getPhone());
        map.put("status", "");
        map.put("imageURL", "default");
        mRef.child("users").child(currentUserId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    callback.onSuccess();
                } else {
                    callback.onFail();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BUG", e.getMessage() + "");
            }
        });
    }
}
