package com.droid.yutani.myinsta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.droid.yutani.myinsta.adapter.PostAdapter;
import com.droid.yutani.myinsta.model.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    @BindView(R.id.feed) RecyclerView recyclerView;
    public LinearLayoutManager linearLayoutManager;
    public PostAdapter postAdapter;

    private List<Post> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        mPosts = new ArrayList<>();
        mPosts.add(new Post("https://komonews.com/resources/media/ee4159df-a62b-41b8-ab27-6557ef922518-large16x9_dn11Air4Mt.Rainier_frame_3111.jpg?1532888163148", "yutani", "Mt. Rainier, 14,441ft"));

        linearLayoutManager = new LinearLayoutManager(this);
        postAdapter = new PostAdapter();
        postAdapter.setPosts(mPosts);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(postAdapter);
    }

    public void post() {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }
}
