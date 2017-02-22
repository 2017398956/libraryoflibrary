package com.nfl.libraryoflibrary.utils.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * Created by fuli.niu on 2016/7/14.
 * 这里的try catch暂时解决崩溃问题
 */
public class ImageLoadTool {

    public static void loadImageByPath(Context context, String filePath, ImageView view) {
        File file = new File(filePath);
        if (file.exists()) {
            Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
        }
    }

    /**
     * @param context
     * @param imageUrl
     * @param view
     * @param defaultResID               当其值为0时，不设置默认图片
     * @param customBitmapTransformation 当其为null时，不进行图片变形
     */
    public static void loadImage(Activity context, String imageUrl, ImageView view, int defaultResID, CustomBitmapTransformation customBitmapTransformation) {
        try {
            imageUrl += "";
        if (0 == defaultResID) {
            if (null == customBitmapTransformation) {
                Glide.with(context).load(Uri.parse(imageUrl)).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                Glide.with(context).load(Uri.parse(imageUrl)).transform(customBitmapTransformation).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            }
        } else {
            if (null == customBitmapTransformation) {
                Glide.with(context).load(Uri.parse(imageUrl)).error(defaultResID).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                Glide.with(context).load(Uri.parse(imageUrl)).error(defaultResID).transform(customBitmapTransformation).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            }
        }
        } catch (Exception e) {
        }
    }

    public static void loadImage(Context context, String imageUrl, ImageView view, int defaultResID, CustomBitmapTransformation customBitmapTransformation) {
        try {
            Activity contextReplace = (Activity) context;
            imageUrl += "";
        if (0 == defaultResID) {
            if (null == customBitmapTransformation) {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).transform(customBitmapTransformation).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            }
        } else {
            if (null == customBitmapTransformation) {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).error(defaultResID).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).error(defaultResID).transform(customBitmapTransformation).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void loadImage(Context context, String imageUrl, ImageView view, Drawable drawable, CustomBitmapTransformation customBitmapTransformation) {
        try {
            Activity contextReplace = (Activity) context;
            imageUrl += "";
        if (null == drawable) {
            if (null == customBitmapTransformation) {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).transform(customBitmapTransformation).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            }
        } else {
            if (null == customBitmapTransformation) {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).error(drawable).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                    Glide.with(contextReplace).load(Uri.parse(imageUrl)).error(drawable).transform(customBitmapTransformation).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
                }
            }
        } catch (Exception e) {
        }
    }
}
