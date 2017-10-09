package com.nfl.libraryoflibrary.view;

import android.os.Bundle;

import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.view.activity.CommonActionBarActivity;

public class BaseActivity extends CommonActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.i("ClassName:" + getClass().getName());
    }
}
