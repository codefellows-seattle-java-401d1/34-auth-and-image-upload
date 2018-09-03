package com.android.sooz.catstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_SAVE_PHOTO = 1;

    private Bitmap mBitmap = null;
    private String mCurrentPhotoPath = null;

    private StorageReference mStorageRef;

    private FirebaseAuth mAuth;

    @BindView(R.id.preview)
    public ImageView mPreview;

    @BindView(R.id.description)
    public EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        dispatchTakePictureIntent();
    }

    //from my project 31
    public void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.android.sooz.catstagram",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_SAVE_PHOTO);
            }
        }
    }

    //from my project 31
    @OnClick(R.id.upload)
    public void uploadFile(){

        //in case the phone didn't take a picture correctly
        if(mBitmap == null){
            return;
        }

        StorageReference photoRef = mStorageRef.child("photos" + mCurrentPhotoPath);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        photoRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        //from referring to Amy Cohen's code and
                        //for when database connected - NOT YET
                        UploadActivity.this.saveImageUrlToDatabase(downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                    }
                });
    }

    //save photo storage info to the application database
    private void saveImageUrlToDatabase(Uri storageUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();
        String description = mDescription.getText().toString();

        Log.d("UPLOAD: ", "user id " + uid + "photo description: " + description);

        //Put info into the database - instead of storing within the app
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference photosRef = database.getReference("photos");

        DatabaseReference newPhoto = photosRef.push();
        newPhoto.child("uid").setValue(uid);
        newPhoto.child("description").setValue(description);
        newPhoto.child("imageUrl").setValue(storageUrl.toString());

        populateFeed();

    }
    //taken from prior lab
    //need to help application actually capture photo and save to Firebase storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_SAVE_PHOTO && resultCode == RESULT_OK){
            setPictureFromFile();
        }
    }


    //saves taken picture as an image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".bmp",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPictureFromFile(){

        //first get the dimensions of of the view where image will be populated
        int targetWidth = mPreview.getWidth();
        int targetHeight = mPreview.getHeight();

        //change picture from bitmap object stored in Firebase Storage to bitmap object for app
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bitmapOptions);

        //set dimensions of picture
        int photoWidth = bitmapOptions.outWidth;
        int photoHeight = bitmapOptions.outHeight;

        //in case either target dimension is 0
        if(targetHeight == 0){
            targetHeight = targetWidth;
        }

        if(targetWidth == 0){
            targetWidth = targetHeight;
        }

        //determine how much to scale down/up image
        int scaleFactor = Math.min(photoWidth/targetWidth, photoHeight/targetHeight);

        //decode image from bitmap stored in Firebase storage
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;
        bitmapOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bitmapOptions);

        mPreview.setImageBitmap(bitmap);

        //from Amy Cohen's project 34...just in case
        mBitmap = bitmap;
        //from project 31
        uploadFile();
    }

    //method to actually fill up feed view and go there
    public void populateFeed(){
        Intent intent = new Intent(this, FeedActivity.class);

        //editor doesn't like this. maybe don't need to pass username between
        //every activity? holding on to this for later review, just in case
//        intent.putExtra(mAuth.getCurrentUser().getUid());
        startActivity(intent);
    }

}
