package com.nfl.libraryoflibrary.utils.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.nfl.libraryoflibrary.utils.LogTool;

import java.io.File;

/**
 * Created by fuli.niu on 2016/7/14.
 * 这里的try catch暂时解决崩溃问题
 */
public class ImageLoadTool {

    private static final DiskCacheStrategy commonDiskCacheStrategy = DiskCacheStrategy.ALL;// 统一管理缓存参数

    public static void loadImageByPath(Context context, String filePath, ImageView view) {
        RequestOptions options = new RequestOptions().diskCacheStrategy(commonDiskCacheStrategy);
        File file = new File(filePath);
        if (file.exists()) {
            Glide.with(context).load(file).apply(options).into(view);
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
            RequestOptions options = new RequestOptions().diskCacheStrategy(commonDiskCacheStrategy);
            if (0 != defaultResID) {
                options.error(defaultResID);
            }
            if (null != customBitmapTransformation) {
                options.transform(customBitmapTransformation);
        }
            Glide.with(context).load(Uri.parse(imageUrl)).apply(options).into(view);
        } catch (Exception e) {
            LogTool.i("Glide 转换图片错误 --> activity");
        }
    }

    public static void loadImage(Context context, String imageUrl, ImageView view, int defaultResID, CustomBitmapTransformation customBitmapTransformation) {
        try {
            Activity contextReplace = (Activity) context;
            imageUrl += "";
            RequestOptions options = new RequestOptions().diskCacheStrategy(commonDiskCacheStrategy);
            if (0 != defaultResID) {
                options.error(defaultResID);
                }
            if (null != customBitmapTransformation) {
                options.transform(customBitmapTransformation);
            }
            Glide.with(contextReplace).load(Uri.parse(imageUrl)).apply(options).into(view);
        } catch (Exception e) {
            LogTool.i("Glide 转换图片错误 --> context");
        }
    }

    public static void loadImage(Context context, String imageUrl, ImageView view, Drawable drawable, CustomBitmapTransformation customBitmapTransformation) {
        try {
            Activity contextReplace = (Activity) context;
            imageUrl += "";
            RequestOptions options = new RequestOptions().diskCacheStrategy(commonDiskCacheStrategy);
            if (null != drawable) {
                options.error(drawable);
            }
            if (null != customBitmapTransformation) {
                options.transform(customBitmapTransformation);
            }
            Glide.with(contextReplace).load(Uri.parse(imageUrl)).apply(options).into(view);
        } catch (Exception e) {
            LogTool.i("Glide 转换图片错误 --> drawable");
        }
    }

    public static void loadImage(Context context, int resId, ImageView view, int defaultResID, CustomBitmapTransformation customBitmapTransformation) {
        try {
            RequestOptions options = new RequestOptions().diskCacheStrategy(commonDiskCacheStrategy);
            if (0 != defaultResID) {
                options.error(defaultResID);
            }
            if (null != customBitmapTransformation) {
                options.transform(customBitmapTransformation);
            }
            Glide.with(context).asDrawable().load(resId).apply(options).into(view);
        } catch (Exception e) {
            LogTool.i("Glide 转换图片错误 --> activity");
        }
    }

    public static void loadGif(Context context, int[] resIds, ImageView view, int defaultResID, CustomBitmapTransformation customBitmapTransformation) {
        try {
            RequestOptions options = new RequestOptions().diskCacheStrategy(commonDiskCacheStrategy);
            if (0 != defaultResID) {
                options.error(defaultResID);
            }
            if (null != customBitmapTransformation) {
                options.transform(customBitmapTransformation);
            }
            RequestBuilder<GifDrawable> requestBuilder = null;
            if (null != resIds && resIds.length > 0) {
                for (int resId : resIds) {
                    if (null == requestBuilder) {
                        requestBuilder = Glide.with(context).asGif().load(resId);
            } else {
                        requestBuilder.load(resId);
                    }
                }
            }
            if (null != requestBuilder) {
                requestBuilder.apply(options).into(view);
            }
        } catch (Exception e) {
            LogTool.i("Glide 转换图片错误 --> activity");
        }
    }

    public static void loadGif(Context context, int resId, ImageView view, int defaultResID, CustomBitmapTransformation customBitmapTransformation) {
        try {
            RequestOptions options = new RequestOptions().diskCacheStrategy(commonDiskCacheStrategy);
            if (0 != defaultResID) {
                options.error(defaultResID);
                }
            if (null != customBitmapTransformation) {
                options.transform(customBitmapTransformation);
            }
            Glide.with(context).asGif().load(resId).apply(options).into(view);
        } catch (Exception e) {
            LogTool.i("Glide 转换图片错误 --> activity");
        }
    }

}
