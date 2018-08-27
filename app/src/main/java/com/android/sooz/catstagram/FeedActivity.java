package com.android.sooz.catstagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    private StorageReference mStorageRef;

    private FirebaseAuth mAuth;

    @BindView(R.id.logout)
    public Button mLogout;

    @BindView(R.id.upload)
    public Button mUpload;

    @BindView(R.id.feed)
    public RecyclerView recyclerView;
    public LinearLayoutManager linearLayoutManager;
    public FeedAdapter postAdapter;

    private List<Post> mPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //instantiate a new list of posts
        mPosts = new ArrayList<>();

        //instantiate layout manager variable using context of content
        //from this instance of this view
        linearLayoutManager = new LinearLayoutManager(this);

        //instantiate a new post adapter to populate posts in feed
        postAdapter = new PostAdapter();

        //populate postAdapter with new list of posts
        postAdapter.setPosts(mPosts);

        //populate the recyclerview of this activity with the content
        //from this view now that it has a new list of posts
        recyclerView.setLayoutManager(linearLayoutManager);

        //actually populate the posts, using the postAdapter, into the
        //recycler view within the feed activity view layout
        recyclerView.setAdapter(postAdapter);
    }

    //allows users to go to UploadActivity to post new photo
    @OnClick(R.id.post)
    public void post(){
        Log.d("POST", "posting");
        Intent intent = new Intent (this, UploadActivity.class);

        //pass user info to upload page so it can be used to be associated
        //with uploaded photo in storage
        String userId = mAuth.getCurrentUser().getUid();
        String username = mAuth.getCurrentUser().getEmail();

        //take care of anonymous user case
        if (username == null){
            username = "anonymous";
        }

        intent.putExtra(username, userId);
        startActivity(intent);
    }

    //so users can logout from the feed view page and return to the login page
    @OnClick(R.id.logout)
    public void logout() {
        String text = mAuth.getCurrentUser().getEmail() + " you are being logged out";
        String textAnon = mAuth.getCurrentUser().getUid()+ " you are being logged out";

        //for email logged in users
        if(mAuth.getCurrentUser().getEmail() != null) {
            //let user know what is happening by clicking this button
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

        //for anonymous logged in users
        if(mAuth.getCurrentUser().getEmail() == null) {
            //let user know what is happening by clicking this button
            Toast.makeText(this, textAnon, Toast.LENGTH_SHORT).show();
        }


        //log user out and take them back to login view
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
