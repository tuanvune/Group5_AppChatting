package com.Chatting.chatapp.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Chatting.chatapp.Presenter.EditPresenter;
import com.Chatting.chatapp.Presenter.View.IEditView;
import com.Chatting.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements IEditView {
    public static CircleImageView ciProfile;
    public static EditText edtUsername, edtStatus;
    private Button btnEdit;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private String currentUserId;
    private EditPresenter editPresenter;
    private ProgressDialog dialog;

    private static final int image_pick = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        initWidget();
        initDB();
        getDataProfile();
        editProfile();
        selectImage();
    }

    private void selectImage() {
        ciProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPresenter.selectImage(image_pick);
            }
        });
    }

    private void getDataProfile() {
        editPresenter.getDataUser(edtUsername, edtStatus, ciProfile);
    }

    private void editProfile() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String status = edtStatus.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    edtUsername.setError("Username is required");
                    edtUsername.requestFocus();
                    return;
                } else {
                    editPresenter.updateDataUser(EditProfileActivity.this, username, status);
                }
            }
        });
    }

    private void initWidget() {
        ciProfile = findViewById(R.id.ciEdit);
        edtUsername = findViewById(R.id.edtUsername);
        edtStatus = findViewById(R.id.edtStatus);
        btnEdit = findViewById(R.id.btnEdit);
        dialog = new ProgressDialog(this);

        Toolbar toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDB() {
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        currentUserId = auth.getCurrentUser().getUid();
        editPresenter = new EditPresenter(EditProfileActivity.this, auth, rootRef, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editPresenter.uploadImage(requestCode, resultCode, data, image_pick, ciProfile);
    }

    @Override
    public void showProgress() {
        dialog.setTitle("Updating");
        dialog.setMessage("Please wait minutes");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }
}
