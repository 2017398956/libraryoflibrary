package com.nfl.libraryoflibrary.utils.net;

import com.google.gson.Gson;
import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public abstract class CustomCallBack2<T> implements CustomCallBackInterface2<T> , Callback {

    private Class<T> clz ;

    public CustomCallBack2(){
    }

    @Override
    public abstract void failure();

    @Override
    public abstract void success(T result);

    @Override
    public void onFailure(Call call, IOException e) {
        CustomProgressBarDialog.dimissProgressBarDialog();
        failure();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        CustomProgressBarDialog.dimissProgressBarDialog();
        if(response.isSuccessful()) {
            String resultTemp = response.body().string() ;
            if(null == resultTemp || "".equals(resultTemp)){
                return;
            }
        success(new Gson().fromJson(response.body().string() , clz));
        }else {
        }
    }

}
