package com.nfl.libraryoflibrary.utils.image;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by fuli.niu on 2016/7/25.
 */
public class CustomBitmapTransformation extends BitmapTransformation {

    private static final String ID = "com.nfl.libraryoflibrary.utils.image.CustomBitmapTransformation";
    private static final byte[] ID_BYTES = ID.getBytes();
    private Context context ;

    public CustomBitmapTransformation(Context context){
        this.context = context ;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return null;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
