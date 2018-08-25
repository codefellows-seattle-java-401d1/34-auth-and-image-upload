package com.android.sooz.catstagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN";

    private FirebaseAuth mAuth;

    //Options when not logged in (i.e. Logged out)

    @BindView(R.id.loggedOutOptions)
    public Layout mLoggedOutOptions;

    @BindView(R.id.email)
    public TextView mEmail;

    @BindView(R.id.passwordInput)
    public TextView mPassword;

    @BindView(R.id.emailLogin)
    public Button mEmailLogin;

    @BindView(R.id.anonLogin)
    public Button mAnonLogin;

    //Options when already logged in

    @BindView(R.id.loggedInOptions)
    public Layout mLoggedInOptions;

    @BindView(R.id.usernameInfoLabel)
    public TextView mSignedInAs;

    @BindView(R.id.usernameInfo)
    public TextView mUsernameInfo;

    @BindView(R.id.proccedToFeed)
    public Button mProccedToFeed;

    @BindView(R.id.logout)
    public Button mLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user) {

        if (user != null && user.getUid() != null) {
            if (user.getEmail() != null) {
                mUsernameInfo.setText(user.getEmail() + " : " + user.getUid());
            } else {
                mUsernameInfo.setText("Anonymous: " + user.getUid());
            }
            showLogout();
        } else {
            mUsernameInfo.setText(R.string.must_be_logged_in);
            showLogin();
        }
    }

    public void showLogout() {
        mEmail.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mEmailLogin.setVisibility(View.GONE);
        mAnonLogin.setVisibility(View.GONE);

        mSignedInAs.setVisibility(View.VISIBLE);
        mUsernameInfo.setVisibility(View.VISIBLE);
        mProccedToFeed.setVisibility(View.VISIBLE);
        mLogout.setVisibility(View.VISIBLE);
    }

    public void showLogin() {
        mEmail.setVisibility(View.VISIBLE);
        mPassword.setVisibility(View.VISIBLE);
        mEmailLogin.setVisibility(View.VISIBLE);
        mAnonLogin.setVisibility(View.VISIBLE);

        mSignedInAs.setVisibility(View.GONE);
        mUsernameInfo.setVisibility(View.GONE);
        mProccedToFeed.setVisibility(View.GONE);
        mLogout.setVisibility(View.GONE);
    }

    @OnClick(R.id.emailLogin)
    public void emailLogin() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //report sign in success with signed in info
                            Log.d(TAG, "createUserWithEmail: successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            //toast set up review in lecture and from
                            //https://developer.android.com/guide/topics/ui/notifiers/toasts
                            CharSequence text = "successfully logged in as: " + user;
                            Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                        } else {
                            //if logging in fails, tell the user and developer
                            Log.d(TAG, "createUserWithEmail: failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @OnClick(R.id.anonLogin)
    public void anonLogin() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //report sign in success with signed in info
                            Log.d(TAG, "createAnonymousUser: successful");

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            //toast set up review in lecture and from
                            //https://developer.android.com/guide/topics/ui/notifiers/toasts
                            CharSequence text = "successfully logged in anonymously as: " + user;
                            Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.proccedToFeed)
    public void proceedtoFeed() {
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = new Intent(this, FeedActivity.class);
        intent.putExtra("uid", user.getUid());
        startActivity(intent);
    }

    @OnClick(R.id.logout)
    public void logout() {
        mAuth.signOut();
        updateUI(null);
    }

    //adding from Friday lecture so I can see activity in Logcat
    @Override
    public void onStart() {
        super.onStart();
        Log.d("LIFECYCLE", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LIFECYCLE", "onResume");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("LIFECYCLE", "onRestart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LIFECYCLE", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("LIFECYCLE", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "onDestroy");
    }
}


