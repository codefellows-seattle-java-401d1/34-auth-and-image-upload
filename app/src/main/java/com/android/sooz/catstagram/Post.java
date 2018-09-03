package com.android.sooz.catstagram;

public class Post {

    public String randomKey;
    public String imageUrl;
    public String description;
    public String uid;

    public Post(String randomKey, String imageUrl, String description, String uid){
        this.randomKey = randomKey;
        this.imageUrl = imageUrl;
        this.description = description;
        this.uid = uid;
    }

}
