package com.nfl.libraryoflibrary.utils.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by fuli.niu on 2016/7/25.
 */
public class CustomBitmapTransformation extends BitmapTransformation {

    public CustomBitmapTransformation(Context context){
        super(context) ;
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }
}
