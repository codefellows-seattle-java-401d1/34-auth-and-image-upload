package assignment.panos_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private FirebaseAuth mAuth;

    @BindView(R.id.signedInAs)
    TextView mSignedInAs;

    @BindView(R.id.usernameInfo)
    TextView mUsernameInfo;

    @BindView(R.id.emailField)
    EditText mEmailField;

    @BindView(R.id.passwordField)
    EditText mPasswordField;

    @BindView(R.id.signin)
    Button mSignin;

    @BindView(R.id.signinAnonymously)
    Button mSignInAnonymously;

    @BindView(R.id.proceedToFeed)
    Button mContinueOn;

    @BindView(R.id.logout)
    Button mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }
    public void updateUI(FirebaseUser user) {
        if(user != null && user.getUid()!=null){
            if(user.getEmail()!=null){
                mUsernameInfo.setText(user.getEmail() + " " + user.getUid());
            }else{
                mUsernameInfo.setText("Anonymous " + user.getUid());
            }
            showLogout();
        }else{
            mUsernameInfo.setText(R.string.must_be_logged_in);
            showLogin();
        }
    }
    public void showLogin(){
        mEmailField.setVisibility(View.VISIBLE);
        mPasswordField.setVisibility(View.VISIBLE);
        mSignin.setVisibility(View.VISIBLE);
        mSignInAnonymously.setVisibility(View.VISIBLE);

        mSignedInAs.setVisibility(View.GONE);
        mUsernameInfo.setVisibility(View.GONE);
        mContinueOn.setVisibility(View.GONE);
        mLogoutButton.setVisibility(View.GONE);
    }

    public void showLogout(){
        mEmailField.setVisibility(View.GONE);
        mPasswordField.setVisibility(View.GONE);
        mSignin.setVisibility(View.GONE);
        mSignInAnonymously.setVisibility(View.GONE);

        mSignedInAs.setVisibility(View.VISIBLE);
        mUsernameInfo.setVisibility(View.VISIBLE);
        mContinueOn.setVisibility(View.VISIBLE);
        mLogoutButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.signin)
    public void signing(){
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Toast.makeText(LoginActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @OnClick(R.id.signinAnonymously)
    public void anonymousSignIn(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                    }
                });
    }

    @OnClick(R.id.proceedToFeed)
    public void setProceedtoFeed(){
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logout)
    public void logout(){
        mAuth.signOut();
        updateUI(null);
    }
}
