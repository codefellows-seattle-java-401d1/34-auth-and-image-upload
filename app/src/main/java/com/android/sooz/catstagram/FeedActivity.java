package com.android.sooz.catstagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    @BindView(R.id.feed)
    public RecyclerView recyclerView;
    public LinearLayoutManager linearLayoutManager;
    public PostAdapter postAdapter;

    private List<Post> mPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

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

        Intent intent = new Intent (this, UploadActivity.class)
        startActivity(intent);
    }
}
