package assignment.panos_lab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_SAVE_PHOTO = 1;
    public final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @BindView(R.id.imagePreview)
    ImageView mImageView;

    private String currImgPath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ButterKnife.bind(this);

        dispatchTakePictureIntent();
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
        try{
            photoFile = createImageFile();
        }catch(IOException e) {
            e.printStackTrace();
        }
        if(photoFile != null){
            photoUri = FileProvider.getUriForFile(this,"assignment.panos_lab",photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(takePictureIntent, REQUEST_SAVE_PHOTO);
        }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SAVE_PHOTO && resultCode == RESULT_OK){
            setPictureFromFile();
            galleryAddPic();
        }
    }

    public void setPictureFromThumbnail(Intent data){
        Bundle extras = data.getExtras();
        if (extras != null){
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {
        String dateForName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "JPEG_" + dateForName + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageName,".jpg",storageDir);
        currImgPath = image.getAbsolutePath();
        return image;
    }
    private void setPictureFromFile() {
        int targetW = mImageView.getMaxWidth();
        int targetH = mImageView.getMaxHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currImgPath,bmOptions);

        if(targetH == 0){
            targetH = 1;
        }
        if(targetW ==0){
            targetW = 1;
        }

        int scaleFactor = Math.min(bmOptions.outWidth/targetW,bmOptions.outHeight/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap  = BitmapFactory.decodeFile(currImgPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currImgPath);
        URI contentURI = URI.create(f.getAbsolutePath());
        Uri contentUri = Uri.parse(contentURI.toString());
//        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        Bitmap currImg = BitmapFactory.decodeFile(contentUri.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        currImg.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        String uploadName = f.getName().substring(0,f.getName().length()-5);

        DatabaseReference myRef = database.getReference("photo/");
        DatabaseReference sendLocation = myRef.child(uploadName);
        sendLocation.setValue(base64Image);
    }
}
