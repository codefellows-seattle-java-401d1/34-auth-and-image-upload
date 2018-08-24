package com.amycohen.lab34authimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.takePicture)
    public void takePicture() {
        Intent intent = new Intent(this, PhotoUploadActivity.class);
        startActivity(intent);
    }

}
