package com.android.sooz.catstagram;

import android.net.Uri;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.tasks.Task;

@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    Task<Uri> url = mStorageRef.getDownloadUrl();

    GlideApp.with(FeedFragment).load(url).centerCrop().placeholder(R.drawable.loading).into(myImageView);
}
