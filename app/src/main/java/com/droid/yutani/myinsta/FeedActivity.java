package com.droid.yutani.myinsta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.droid.yutani.myinsta.adapter.PostAdapter;
import com.droid.yutani.myinsta.model.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    @BindView(R.id.feed) public RecyclerView recyclerView;
    public LinearLayoutManager linearLayoutManager;
    public PostAdapter postAdapter;

    private List<Post> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        mPosts = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        postAdapter = new PostAdapter();
        postAdapter.setPosts(mPosts);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(postAdapter);
    }

    @OnClick(R.id.upload)
    public void post() {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }
}
