package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by fuli.niu on 2016/8/5.
 * 弹出Dialog从相册获取图片或者通过照相功能拍摄图片
 */
public class PictureSelector {

    public static final int REQUESTCODE_SELECT_PICTURE_SUCCESS = 701;
    public static final int REQUESTCODE_TAKE_PHOTO_SUCCESS = 2;
    public static final int REQUESTCODE_CROP_IMAGE_SUCCESS = 702;

    /**
     * 获取头像，以拍照或相册获取照片
     *
     * @param activity  打卡该dialog的对话框，非activity不能打开该dialog
     * @param photoPath 拍照后的存储路径
     *                  路径为：/xxxx/.../xxx.jpg，必须是个文件地址，不能使目录；
     *                  使用拍照功能时，为照片的储存路径（为了解决部分机型返回图片数据为空而存储在SDCard上）；
     *                  从相册选择图片时，不具有实际用处。
     *                  存储成功后可使用{@link #imageCrop(Activity, String, String)}剪裁图片
     */
    public static void showDialog(final Activity activity, final String photoPath) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_picture_selector, null);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_take_photo = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView tv_select_picture = (TextView) view.findViewById(R.id.tv_select_picture);

        DialogTool.getInstance().displayDialog(activity, view, Gravity.BOTTOM);
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogTool.getInstance().dismissDialog();
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogTool.getInstance().dismissDialog();
                // 拍照
                goTakePhoto(activity, photoPath);
            }
        });
        tv_select_picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogTool.getInstance().dismissDialog();
                goPhotoPick(activity); // 选择图片
            }
        });
    }

    /**
     * 多选模式
     *
     * @param activity
     * @param photoPath
     * @param intent    将要被打开的Activity
     */
    public static void showDialog4MultiSelect(final Activity activity, final String photoPath, final Intent intent, final int requestCode) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_picture_selector, null);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_take_photo = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView tv_select_picture = (TextView) view.findViewById(R.id.tv_select_picture);

        DialogTool.getInstance().displayDialog(activity, view, Gravity.BOTTOM);
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogTool.getInstance().dismissDialog();
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogTool.getInstance().dismissDialog();
                // 拍照
                goTakePhoto(activity, photoPath, requestCode);
            }
        });
        tv_select_picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogTool.getInstance().dismissDialog();
                if (null != intent) {
                    activity.startActivityForResult(intent, requestCode);
                }
            }
        });
    }

    /**
     * 从相册中选择图片
     */
    private static void goPhotoPick(Activity activity) {
        if (!FileTool.isSDCardMounted()) {
            ToastTool.showShortToast("SD卡未准备");
        } else if (FileTool.getSDFreeSize() < 2) {
            ToastTool.showShortToast("SD卡未准备");
        } else {
            /**
             * Intent.ACTION_PICK , 好像会new task
             * intent = new Intent(Intent.ACTION_OPEN_DOCUMENT) ;好像会new task
             */
//            Intent intent = new Intent(
//                    Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent intent;
            if (Build.VERSION.SDK_INT >= 19) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            activity.startActivityForResult(intent, REQUESTCODE_SELECT_PICTURE_SUCCESS);
        }
    }

    /**
     * 使用照相机拍摄图片
     */
    private static Intent goTakePhotoCommon(Activity activity, String photoPath) {
        if (!FileTool.isSDCardMounted()) {
            ToastTool.showShortToast("SD卡未准备");
        } else if (FileTool.getSDFreeSize() < 2) {
            ToastTool.showShortToast("SD卡空间不足");
        } else {
            LogTool.i("拍照路径：" + photoPath);
            File imageFile = new File(photoPath);
            if (!imageFile.getParentFile().exists()) {
                imageFile.getParentFile().mkdirs();
            }
            if (!imageFile.exists()) {
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    LogTool.i("PictureSelector拍照失败");
                    e.printStackTrace();
                }
            }
            Uri imageFileUri = Uri.fromFile(imageFile);

            Intent intent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
            return intent;
        }
        return null;
    }

    private static void goTakePhoto(Activity activity, String photoPath) {
        Intent intent = goTakePhotoCommon(activity, photoPath);
        if (null != intent) {
            activity.startActivityForResult(intent, REQUESTCODE_TAKE_PHOTO_SUCCESS);
        }
    }

    private static void goTakePhoto(Activity activity, String photoPath, int requestCode) {
        Intent intent = goTakePhotoCommon(activity, photoPath);
        if (null != intent) {
            // intent.putExtra("imagePath" , photoPath) ;
            activity.startActivityForResult(intent, requestCode * 10);
        }
    }

    /**
     * 图片裁剪
     *
     * @param activity   打卡该dialog的对话框，非activity不能打开该dialog
     * @param sourcePath 图片数据来源路径
     * @param photoPath  图片剪裁后的存储路径
     *                   路径为：/xxxx/.../xxx.jpg，必须是个文件地址，不能使目录；
     *                   可以和{@link #showDialog(Activity, String)}中的 photoPath 一致
     */

    public static void imageCrop(Activity activity, String sourcePath, String photoPath) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(Uri.fromFile(new File(sourcePath)), "image/*");
//            cropIntent.setDataAndType(picUri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            cropIntent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
//            cropIntent.putExtra("aspectX", 16);
//            cropIntent.putExtra("aspectY", 9);
            cropIntent.putExtra("aspectX", PhoneInfoTool.getScreenWidth(activity));
            cropIntent.putExtra("aspectY", PhoneInfoTool.getScreenHeight(activity));
            // outputX,outputY 是剪裁图片的宽高
            cropIntent.putExtra("outputX", PhoneInfoTool.getScreenWidth(activity) * 2 / 3);
            cropIntent.putExtra("outputY", PhoneInfoTool.getScreenHeight(activity) * 2 / 3);
//            cropIntent.putExtra("outputX", mScreenWidth);
            // cropIntent.putExtra("outputY", (int) (9.0F * (mScreenWidth / 16.0F)));
            cropIntent.putExtra("scale", true);
            /**
             * 此方法返回的图片只能是小图片（传递true时。sumsang测试为高宽160px的图片）
             * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
             */
            cropIntent.putExtra("return-data", false);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
            // 不建议用路径的方式，路径方式需要加“file://"
            // cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            activity.startActivityForResult(cropIntent, REQUESTCODE_CROP_IMAGE_SUCCESS);
        } catch (ActivityNotFoundException anfe) {
            LogTool.i("不支持裁剪");
        }
    }


}
