package com.amycohen.lab34authimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loggedInOptions) LinearLayout mLoggedInOptions;
    @BindView(R.id.loggedOutOptions) LinearLayout mLoggedOutOptions;
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
        }
    }
}
