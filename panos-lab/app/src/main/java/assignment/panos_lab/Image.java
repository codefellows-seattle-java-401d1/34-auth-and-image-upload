package assignment.panos_lab;

import android.content.Intent;
import android.graphics.Bitmap;

import java.io.IOException;

public class Image {
    public String name;
    public String url;
    public String description;
    public Bitmap picture;

    public Image(String url, String description, String name){
        final String tempString = url;
        final Image tempImage = this;
        this.url = url;
        this.description = description;
        this.name = name;
        new Thread(new Runnable(){
            public void run(){
                MyAsync mAsync = new MyAsync();
                try {
                    mAsync.doInBackground(tempString,tempImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void fillIntent(Intent intent){
        intent.putExtra("name",this.name);
        intent.putExtra("url",this.url);
        intent.putExtra("description",this.description);
    }
    public static Image fromIntent(Intent data){
        return new Image(
                data.getStringExtra("name"),
                data.getStringExtra("url"),
                data.getStringExtra("description")
        );
    }
}
