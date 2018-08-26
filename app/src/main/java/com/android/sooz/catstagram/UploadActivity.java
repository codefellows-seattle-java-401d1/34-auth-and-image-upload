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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

    private Bitmap mBitmap;
    private String mCurrentPhotoPath;

    private StorageReference mStorageRef;

    private FirebaseAuth mAuth;

    @BindView(R.id.preview)
    public ImageView mPreview;

    @BindView(R.id.description)
    public TextView mDescription;

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

    //saves taken picture as an image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //from my project 31
    @OnClick(R.id.upload)
    public void uploadFile(){

        StorageReference photoRef = mStorageRef.child("photos/" + mCurrentPhotoPath);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        photoRef.putBytes(data)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    //from referring to Amy Cohen's code and
                    //for when database connected - NOT YET
//                    UploadActivity.this.saveImageUrlToDatabase(downloadUrl);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    exception.printStackTrace();
                }
            });
    }

    //need to help application actually capture photo and save to Firebase storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_SAVE_PHOTO && resultCode == RESULT_OK){
             setPictureFromFile();
//           galleryAddPic();
        }
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
        //from original project 31 maybe not needed? NOT YET
//        uploadFile(bitmap);
    }

}
