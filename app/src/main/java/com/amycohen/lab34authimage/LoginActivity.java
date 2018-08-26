package com.amycohen.lab34authimage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loggedInOptions) LinearLayout mLoggedInOptions;
    @BindView(R.id.loggedOutOptions) LinearLayout mLoggedOutOptions;
    @BindView(R.id.usernameInfo) TextView mUsernameInfo;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI (FirebaseUser user) {
        if (user == null) {
            mLoggedInOptions.setVisibility(View.GONE);
            mLoggedOutOptions.setVisibility(View.VISIBLE);
        } else {
            mLoggedInOptions.setVisibility(View.VISIBLE);
            mLoggedOutOptions.setVisibility(View.GONE);

            String info = "";
            if (user.getUid() != null && user.getEmail() != null) {
                info = user.getEmail() + " " + user.getUid();
            } else if (user.getUid() != null) {
                info = "anonymous " + user.getUid();
            }
                mUsernameInfo.setText(info);
        }
    }

    @OnClick(R.id.anonymousLogin)
    public void anonymousLogIn() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @OnClick(R.id.proceedToFeed)
    public void proceedToFeed() {
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
}
