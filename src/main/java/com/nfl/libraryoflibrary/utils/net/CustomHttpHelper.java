package com.nfl.libraryoflibrary.utils.net;

import com.nfl.libraryoflibrary.BuildConfig;
import com.nfl.libraryoflibrary.constant.Constants;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public class CustomHttpHelper {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    private static OkHttpClient okHttpClient;

    private static void getInstance() {
        if (null == okHttpClient) {
            synchronized (CustomHttpHelper.class) {
                if (null == okHttpClient) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(3000, TimeUnit.MILLISECONDS)
                            .build();
                }
            }
        }
    }

    public static void getDataFromServer(String url, Map<String, String> keyValuePairs, CustomCallBackInterface customCallBack) {
        getInstance();
        FormBody.Builder builder = new FormBody.Builder();

        if (null != keyValuePairs && keyValuePairs.size() > 0) {
            String[] array = new String[keyValuePairs.size()];
            array = keyValuePairs.keySet().toArray(array);
            for (int i = 0; i < keyValuePairs.size(); i++) {
                builder.add(array[i], keyValuePairs.get(array[i]));
            }
        }

        builder.add("devtype", "Android");// 设备类型（区分安卓和IOS）
        builder.add("appversion", Constants.APPVERSION);// app版本
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue((Callback) customCallBack);
    }

    public static void uploadImg(String imagePath , String url) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File f = new File(imagePath);
        if (f != null) {
            builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
        }
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                CustomProgressBarDialog.dimissProgressBarDialog();
                LogTool.i("上传图片失败") ;
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CustomProgressBarDialog.dimissProgressBarDialog();
                JSONObject jsonObject = null ;
                try {
                    jsonObject = new JSONObject(response.body().string()) ;
                }catch (Exception e){
                    LogTool.i("图片上传失败") ;
                }

                if(null != jsonObject && 0 == jsonObject.optInt("status" , 1)){
                    LogTool.i("图片上传成功");
                }else{
                    LogTool.i("图片上传失败了jsonObject.isNull:" + (null == jsonObject ? "null" : "not null , msg:" + jsonObject.optString("msg" , "error")) ) ;
                }

            }
        });

    }
}
