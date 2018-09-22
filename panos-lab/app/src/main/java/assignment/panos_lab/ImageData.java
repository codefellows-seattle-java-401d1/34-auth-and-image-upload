package assignment.panos_lab;


import java.util.ArrayList;
import java.util.List;

import assignment.panos_lab.Image;

public class ImageData {
    private static ImageData mSingleton;
    private List<Image> images;
    private ImageData(){
        images = new ArrayList<>();
        images.add(new Image("https://i.imgur.com/ttsHSK5.jpg","I like my infants over medium","Tasty"));
        images.add(new Image("https://i.imgur.com/1ilMJwo.jpg","vampire just chillin","hangin out with the boy"));
        images.add(new Image("https://i.imgur.com/4OoXIYQ.png","that's very hot","korean heat indicators"));
    }

    public static ImageData get(){
        if(mSingleton == null){
            mSingleton = new ImageData();
        }
        return mSingleton;
    }

    public List<Image> images() { return images; }
}
