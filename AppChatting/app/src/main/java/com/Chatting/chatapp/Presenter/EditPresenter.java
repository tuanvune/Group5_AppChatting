package com.Chatting.chatapp.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.Model.IEdit;
import com.Chatting.chatapp.Presenter.View.IEditView;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPresenter implements IEdit {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private String currentUserId = "";
    private IEditView callback;
    private static final int RESULT_OK = -1;
    private StorageReference filePath;

    public EditPresenter(Context context, FirebaseAuth mAuth, DatabaseReference mRef, IEditView callback) {
        this.context = context;
        this.mAuth = mAuth;
        this.mRef = mRef;
        this.callback = callback;
    }

    @Override
    public void getDataUser(EditText edtUsername, EditText edtStatus, CircleImageView ciProfile) {
        currentUserId = mAuth.getCurrentUser().getUid();
        mRef.child("users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    String name = snapshot.child("username").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();
                    edtUsername.setText(name);
                    edtStatus.setText(status);
                    if (userModel.getImageURL().equals("default")) {
                        ciProfile.setImageResource(R.drawable.ic_launcher_background);
                    } else {
                        Picasso.get().load(userModel.getImageURL()).into(ciProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void updateDataUser(Activity activity, String name, String status) {
        currentUserId = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", name);
        map.put("status", status);
        callback.showProgress();
        mRef.child("users").child(currentUserId).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            callback.hideProgress();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BUG", e.getMessage());
            }
        });
    }

    @Override
    public void selectImage(int image_code) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, image_code);
        }
    }

    @Override
    public void uploadImage(int requestCode, int resultCode, Intent intent, int image_pick, CircleImageView ciProfile) {
        currentUserId = mAuth.getCurrentUser().getUid();
        if (requestCode == image_pick && resultCode == RESULT_OK && intent != null) {
            Uri imageUri = intent.getData();
            ciProfile.setImageURI(imageUri);
            filePath = FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("profile_image")
                    .child(currentUserId + ".jpg");
            callback.showProgress();
            filePath.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl();
                                callback.hideProgress();
                            }
                        }
                    });
        }

    }

    private void downloadImageUrl() {
        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                HashMap<String, Object> map = new HashMap<>();
                map.put("imageURL", imageURL);
                mRef.child("users")
                        .child(currentUserId).updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    callback.hideProgress();
                                }
                            }
                        });
            }
        });
    }

}
