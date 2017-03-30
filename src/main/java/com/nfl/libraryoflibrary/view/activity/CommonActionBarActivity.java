package com.nfl.libraryoflibrary.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.listener.CustomOnClickListener;

/**
 * Created by fuli.niu on 2017/2/23.
 * 继承自该类的 activity 不需要处理 ActionBar
 */

public abstract class CommonActionBarActivity extends FragmentActivity implements CommonActionBarActivityPretreatments {

    protected Context context;
    private ImageView iv_back;
    private TextView tv_title;
    protected LinearLayout ll_pad_container;
    protected LinearLayout ll_data_binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }

    /**
     * 覆写为了 setContentView 的使用习惯不变
     *
     * @param layoutResID
     */
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_common);
        initActionBarAndViewStub();
        View view = LayoutInflater.from(CommonActionBarActivity.this).inflate(layoutResID, null);
        if (null != view) {
            ll_pad_container.addView(view);
        } else {
            // throw new CustomException("") ;
        }
    }

    public void setContentView(View view) {
        super.setContentView(R.layout.activity_common);
        initActionBarAndViewStub();
        if (null != view) {
            ll_pad_container.addView(view);
        }
    }

    private void initActionBarAndViewStub() {
        ll_pad_container = (LinearLayout) findViewById(R.id.ll_pad_container);
        ll_data_binding = (LinearLayout) findViewById(R.id.ll_data_binding);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new CustomOnClickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                CommonActionBarActivity.this.finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
    }


    /**
     * must call after {@link #setContentView(int)}
     *
     * @param title
     */
    protected void setActionBarTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * must call after {@link #setContentView(int)}
     *
     * @param strId
     */
    protected void setActionBarTitle(int strId) {
        tv_title.setText(getText(strId));
    }
}
