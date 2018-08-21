package com.zd112.framework;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.Serializable;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    public interface ImageLoader extends Serializable {
        void displayImage(Context context, String path, ImageView imageView);
    }
}
