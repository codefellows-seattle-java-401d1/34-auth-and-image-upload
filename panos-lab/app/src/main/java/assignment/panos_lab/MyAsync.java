package assignment.panos_lab;

import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyAsync extends AsyncTask<Void, Void, Void> {
    public static File file;
    InputStream is;

    protected void doInBackground(String urlString, Image whichImage) throws IOException {

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            // Make sure the Pictures directory exists.
            path.mkdirs();

            URL url = new URL(urlString);
            /* Open a connection to that URL. */
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            /*
             * Define InputStreams to read from the URLConnection.
             */
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();

            byte[] data = new byte[is.available()];
            is.read(data);
            whichImage.picture = BitmapFactory.decodeStream(is);
            is.close();

        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
            doInBackground();
        return null;
    }

    protected void onPostExecute() {
            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(null,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    }

            );
    }
}