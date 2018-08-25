package com.amycohen.lab34authimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    @BindView(R.id.feed) RecyclerView feed;
    private LinearLayoutManager linearLayoutManager;
    private FeedAdapter feedAdapter;

    List<Feed> allFeedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

        linearLayoutManager = new LinearLayoutManager(this);
        feedAdapter = new FeedAdapter(allFeedItems);

        feed.setLayoutManager(linearLayoutManager);
        feed.setAdapter(feedAdapter);

    }

    @OnClick(R.id.takePicture)
    public void takePicture() {
        Intent intent = new Intent(this, PhotoUploadActivity.class);
        startActivity(intent);
    }

}
