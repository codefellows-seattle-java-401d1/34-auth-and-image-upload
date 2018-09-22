package assignment.panos_lab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private BitmapAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Bitmap> mBitmaps;
    public final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        mBitmaps = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        DatabaseReference currRef = database.getReference("photo");


        mAdapter = new BitmapAdapter(mBitmaps);

        mRecyclerView.setAdapter(mAdapter);

        attachBitmapListener();
    }

    @OnClick(R.id.cameraButton)
    protected void loadCameraActivity(){
        Intent cameraIntent = new Intent(this,CameraActivity.class);
        startActivity(cameraIntent);
    }

    public void attachBitmapListener(){
        DatabaseReference imgRef = database.getReference("photo");

        imgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Bitmap> photos = new ArrayList<>();
                for(DataSnapshot photo : dataSnapshot.getChildren()){
                    String val = (String) photo.getValue();
                    byte[] val2 = Base64.decode(val,Base64.DEFAULT);
                    Bitmap currImg = BitmapFactory.decodeByteArray(val2,0,val2.length);
                    photos.add(currImg);
                }
                mAdapter.setmBitmaps(photos);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
