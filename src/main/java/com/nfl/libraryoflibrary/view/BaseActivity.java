package com.nfl.libraryoflibrary.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.utils.LogTool;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.i("ClassName:" + getClass().getName()) ;
    }
}
