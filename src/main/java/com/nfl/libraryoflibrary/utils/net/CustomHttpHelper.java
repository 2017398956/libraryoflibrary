package com.nfl.libraryoflibrary.utils.net;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.constant.ApplicationContext;
import com.nfl.libraryoflibrary.constant.Constants;
import com.nfl.libraryoflibrary.utils.ExceptionTool;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.NetUtils;
import com.nfl.libraryoflibrary.utils.ToastTool;
import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    /**
     * 当服务网返回状态码非2xx、3xx时不会调用{@link CustomCallBackInterface}
     * @param url
     * @param keyValuePairs
     * @param customCallBack
     */
    public static void getDataFromServer(String url, Map<String, String> keyValuePairs, CustomCallBackInterface customCallBack) {
        showNetExceptionInfo();
//        if(!NetUtils.isLegalUrl(url)){
//            if(null != customCallBack){
//                customCallBack.failure();
//            }
//            CustomProgressBarDialog.dimissProgressBarDialog();
//            LogTool.i(R.string.url_error);
//            return;
//        }
        boolean hasUserInfo = false ;// 用于标示访问参数中是否含有用户信息
        getInstance();
        FormBody.Builder builder = new FormBody.Builder();

        if (null != keyValuePairs && keyValuePairs.size() > 0) {
            for(String key : keyValuePairs.keySet()){
                if(!hasUserInfo){
                    if("username".equals(key)){
                        hasUserInfo = true ;
                    }
                }
                builder.add(key , keyValuePairs.get(key));
            }
            }
        if(!hasUserInfo){
            // 如果请求服务器的参数中没有用户信息，这里自动加上
            builder.add("username", ApplicationContext.USERNAME);
            builder.add("userid", ApplicationContext.USERID + "");
            builder.add("devid", ApplicationContext.DEVID);
        }

        builder.add("devtype", "Android");// 设备类型（区分安卓和IOS）
        builder.add("appversion", Constants.APPVERSION);// app版本
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue((Callback) customCallBack);
    }

    public static void uploadImg(String imagePath , String url) {
        showNetExceptionInfo();
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
                    LogTool.i("图片上传返回信息：" + jsonObject.toString()) ;
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

    /**
     * 上传文件
     * @param actionUrl 接口地址
     * @param paramsMap 参数
     * @param customCallBack 回调
     */
    public void upLoadFile(String actionUrl, Map<String, Object> paramsMap, CustomCallBackInterface customCallBack) {
        showNetExceptionInfo();
        try {
            //补全请求地址
            String requestUrl = String.format("%s/%s", "upload_head ", actionUrl);
//            if(!NetUtils.isLegalUrl(requestUrl)){
//                if(null != customCallBack){
//                    customCallBack.failure();
//                }
//                CustomProgressBarDialog.dimissProgressBarDialog();
//                LogTool.i(R.string.url_error);
//                return;
//            }
            getInstance();
            boolean hasUserInfo = false ;// 用于标示访问参数中是否含有用户信息
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //创建RequestBody
            RequestBody body = builder.build();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                if(!hasUserInfo){
                    if("username".equals(key)){
                        hasUserInfo = true ;
                    }
                }
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null , file));
                }
            }
            if(!hasUserInfo){
                // 如果请求服务器的参数中没有用户信息，这里自动加上
                builder.addFormDataPart("username", ApplicationContext.USERNAME);
                builder.addFormDataPart("userid", ApplicationContext.USERID + "");
                builder.addFormDataPart("devid", ApplicationContext.DEVID);
            }
            builder.addFormDataPart("devtype", "Android");// 设备类型（区分安卓和IOS）
            builder.addFormDataPart("appversion", Constants.APPVERSION);// app版本
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            okHttpClient.newCall(request).enqueue((Callback) customCallBack);
        } catch (Exception e) {
        }
    }

    /**
     * 多文件上传
     */
    private void uploadMultiFile(String uploadUrl , String... filePaths) {
        showNetExceptionInfo();
        getInstance();
        File file = new File("fileDir", "test.jpg");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "test.jpg", fileBody)
                .build();
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogTool.i("uploadMultiFile() " + ExceptionTool.getExceptionTraceString(e)) ;
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogTool.i("uploadMultiFile() response=" + response.body().string());
            }
        });
    }

    /**
     * 上传多张图片及参数
     * @param reqUrl URL地址
     * @param params 参数
     * @param pic_key 上传图片的关键字
     * @param files  文件
     */
    public void sendMultipart(String reqUrl,Map<String, String> params,String pic_key, List<File> files){
        showNetExceptionInfo();
        getInstance();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(key, params.get(key));
            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null){
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();

        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                call.cancel();
            }
        });
    }

    /**
     * 没有网络时显示提示信息
     */
    private static void showNetExceptionInfo(){
        if (!NetUtils.isNetworkAvaliable(ApplicationContext.applicationContext)) {
            ToastTool.showShortToast(R.string.no_web_service);
            return;
        }
    }



}
