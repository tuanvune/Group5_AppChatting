package com.Chatting.chatapp.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.Model.ISignin;
import com.Chatting.chatapp.Presenter.View.ISigninView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SigninPresenter implements ISignin {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private ISigninView callback;
    private ProgressDialog dialog;
    private Intent intent;

    public SigninPresenter(Context context, FirebaseAuth mAuth, DatabaseReference mRef, ISigninView callback) {
        this.context = context;
        this.mAuth = mAuth;
        this.mRef = mRef;
        this.callback = callback;
    }


    @Override
    public void validUserSignin(UserModel userModel) {
        if(TextUtils.isEmpty(userModel.getEmail())) {
            Toast.makeText(context, "Email incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(userModel.getEmail()).matches()) {
            Toast.makeText(context, "Email is valid!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userModel.getPassword())) {
            Toast.makeText(context, "Password incorrect", Toast.LENGTH_SHORT).show();
            return;
        } else if(userModel.getPassword().length() < 6) {
            Toast.makeText(context, "Password length should be 6 character!", Toast.LENGTH_SHORT).show();
            return;
        }
        signinUser(userModel);
    }

    @Override
    public void signinUser(UserModel userModel) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Logged");
        dialog.setMessage("Please wait minutes");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mAuth.signInWithEmailAndPassword(userModel.getEmail(), userModel.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    dialog.dismiss();
                    callback.onSuccess();
                } else {
                    dialog.dismiss();
                    callback.onFailed();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BUG", e.getMessage());
            }
        });
    }


}
