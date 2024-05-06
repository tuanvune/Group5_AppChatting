package com.Chatting.chatapp.Presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.Chatting.chatapp.Presenter.Model.IMain;
import com.Chatting.chatapp.Presenter.View.IMainView;
import com.Chatting.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainPresenter implements IMain {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private EditText edtGroupName;
    private IMainView callback;
    private String currentUserId = "";

    public MainPresenter(Context context, FirebaseAuth mAuth, DatabaseReference mRef, IMainView callback) {
        this.context = context;
        this.mAuth = mAuth;
        this.mRef = mRef;
        this.callback = callback;
    }

    @Override
    public void verifyUser() {
        currentUserId = mAuth.getCurrentUser().getUid();
        mRef.child("users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void createNewGroup() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        edtGroupName = new EditText(context);
        dialog.setTitle("Create New Group");
        edtGroupName.setHint("Please enter your group name");
        dialog.setView(edtGroupName);
        dialog.setPositiveButton("create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pushNewGroup(edtGroupName);
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();
    }

    @Override
    public void pushNewGroup(EditText edtGroupName) {
        String groupName = edtGroupName.getText().toString().trim();
        HashMap<String, Object> map = new HashMap<>();
        map.put("imageURL", "default");
        mRef.child("groups").child(groupName).setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.navigaActivity();
                        } else {
                            callback.Message();
                        }
                    }
                });
    }

    @Override
    public void navigaView(ViewPager vpMain) {
        vpMain.setCurrentItem(1, true);
    }


}
