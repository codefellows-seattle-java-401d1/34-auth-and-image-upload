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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public static final String TAG = "FIREBASE: ";

    List<Post> mAllPosts;

    DatabaseReference mPublishedPhotos;

    @BindView(R.id.takePicture)
    public Button mTakePicture;

    @BindView(R.id.feed)
    public RecyclerView feed;

    public LinearLayoutManager linearLayoutManager;
    public FeedAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Log.d("FEED ACTIVITY", "OnCreate triggered from Feed Activity");

        ButterKnife.bind(this);


        mPublishedPhotos = FirebaseDatabase.getInstance().getReference();
        mPublishedPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> photoRef = dataSnapshot.child("photos").getChildren();
                List<Post> photoItems = new ArrayList<>();

                for (DataSnapshot photo : photoRef){
                    String randomKey = photo.getKey();
                    String imageUrl = photo.child("imageUrl").getValue(String.class);
                    String description = photo.child("description").getValue(String.class);
                    String uid = photo.child("uid").getValue(String.class);

                    Post feedStatus = new Post(randomKey, imageUrl, description, uid);
                    photoItems.add(feedStatus);
                }
//                Collections.reverse(photoItems);
                postAdapter.replaceList(photoItems);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //instantiate layout manager variable using context of content
        //from this instance of this view
        linearLayoutManager = new LinearLayoutManager(this);

        //instantiate a new list of posts
        mAllPosts = new ArrayList<>();

        //instantiate a new post adapter to populate posts in feed
        postAdapter = new FeedAdapter(mAllPosts);

        feed.setLayoutManager(linearLayoutManager);
        feed.setAdapter(postAdapter);
    }

    //allows users to go to UploadActivity to post new photo
    @OnClick(R.id.takePicture)
    public void takePicture(){
        Log.d("POST", "posting");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();

        Intent intent = new Intent (this, UploadActivity.class);
        intent.putExtra("id", uid);
        startActivity(intent);
    }

}
