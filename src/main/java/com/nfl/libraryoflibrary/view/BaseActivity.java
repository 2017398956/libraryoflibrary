package com.nfl.libraryoflibrary.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nfl.libraryoflibrary.utils.LogTool;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.i("ClassName:" + getClass().getName()) ;
    }
}
