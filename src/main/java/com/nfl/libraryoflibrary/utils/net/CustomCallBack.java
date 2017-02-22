package com.nfl.libraryoflibrary.utils.net;

import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public abstract class CustomCallBack implements CustomCallBackInterface , Callback {

    public boolean dismissProgressDialog(){
        // TODO �ı��Զ��� ProgressDialog �Ƿ��Զ���ʧ��Ĭ���Զ���ʧ
        return true ;
    }

    @Override
    public abstract void failure();

    @Override
    public abstract void success(String result);

    @Override
    public void onFailure(Call call, IOException e) {
        CustomProgressBarDialog.dimissProgressBarDialog();
        failure();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        if(dismissProgressDialog()){
        CustomProgressBarDialog.dimissProgressBarDialog();
        }
        if(response.isSuccessful()) {
        String resultTemp = response.body().string() ;
        if(null == resultTemp || "".equals(resultTemp)){
            return;
        }
            CustomProgressBarDialog.dimissProgressBarDialog();
        success(resultTemp);
        }else {
//            success("���ʷ�����ʧ��-->com.nfl.libraryoflibrary.utils.net.CustomCallBack");
        }
    }
}
