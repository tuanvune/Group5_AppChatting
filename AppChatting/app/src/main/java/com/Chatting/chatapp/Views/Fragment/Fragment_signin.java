package com.Chatting.chatapp.Views.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.Chatting.chatapp.Models.UserModel;
import com.Chatting.chatapp.Presenter.SigninPresenter;
import com.Chatting.chatapp.Presenter.View.ISigninView;
import com.Chatting.chatapp.R;
import com.Chatting.chatapp.Views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class Fragment_signin extends Fragment implements ISigninView {
    private View view;
    private EditText edtEmail, edtPassword;
    private Button btnSignin;
    private TextView txtForgotpassword;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    private SigninPresenter signinPresenter;
    private UserModel userModel;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView google_btn,facebookBtn;
    CallbackManager callbackManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);
        FacebookSdk.sdkInitialize(getContext());
        AppEventsLogger.activateApp(getActivity().getApplication());
        initWidget();
        loginUser();

        auth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        signinPresenter = new SigninPresenter(getContext(), auth, mRef, Fragment_signin.this);
        userModel = new UserModel();
        onGetIntent();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //Toast.makeText(MainActivity.this,"LOGIN SUCCESSFUL",Toast.LENGTH_SHORT).show();

                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                        if(isLoggedIn){
                            navigateToSecondActivityFacebook(accessToken);


                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
          facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
            }
        });
        return view;
    }

    private void loginUser() {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (userModel != null) {
                    userModel.setEmail(email);
                    userModel.setPassword(password);
                    signinPresenter.validUserSignin(userModel);
                }
            }
        });
    }

    private void initWidget() {
        edtEmail = view.findViewById(R.id.edtEmailLogin);
        edtPassword = view.findViewById(R.id.edtPasswordLogin);
        btnSignin = view.findViewById(R.id.btnSignin);
        txtForgotpassword = view.findViewById(R.id.txtForgotpassword);
        google_btn = view.findViewById(R.id.google_btn);
        facebookBtn = view.findViewById(R.id.facebook_btn);

    }

    @Override
    public void onSuccess() {
        edtEmail.getText().clear();
        edtPassword.getText().clear();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onFailed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        alert.setMessage("Email or password incorrect!");
        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    private void onGetIntent() {


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            startActivity(new Intent(getContext(),MainActivity.class));
        }

    }
    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                UpdateUser(acct);
            } catch (ApiException e) {
                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void UpdateUser(GoogleSignInAccount acct) {
        createSuccess(acct);
    }
    public void createSuccess(GoogleSignInAccount acct) {

      String  currentUserId = acct.getId();
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", currentUserId);
        map.put("username", acct.getDisplayName());
        map.put("email", acct.getEmail());
        map.put("phone", "+84.......");
        map.put("status", "");
        map.put("imageURL", "default");
        mRef.child("users").child(currentUserId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BUG", e.getMessage() + "");
            }
        });
    }
    public void createSuccessFB(String name, String id , String url) {


        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", id);
        map.put("username",name);
        map.put("email", "");
        map.put("phone", "+84.......");
        map.put("status", "");
        map.put("imageURL", url);
        mRef.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       startActivity(new Intent(getContext(),MainActivity.class));
                   }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BUG", e.getMessage() + "");
            }
        });
    }
    private void navigateToSecondActivityFacebook(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        try {
                            //Toast.makeText(MainActivity.this,object.toString(),Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this,object.getString("name"),Toast.LENGTH_SHORT).show();
                            //Log.i("FB", object.toString());
                            String name = object.getString("name");
                            String id = object.getString("id");
                            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            createSuccessFB(name,id,url);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }
}

