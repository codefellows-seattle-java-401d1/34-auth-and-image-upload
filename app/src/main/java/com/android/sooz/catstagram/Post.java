package com.android.sooz.catstagram;

public class Post {

    public String randomKey;
    public String imageUrl;
    public String username;
    public String description;

    public Post(String randomKey, String imageUrl, String username, String description){
        this.randomKey = randomKey;
        this.imageUrl = imageUrl;
        this.username = username;
        this.description = description;
    }

}
