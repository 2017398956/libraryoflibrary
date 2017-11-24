package com.nfl.libraryoflibrary.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfl.apt.annotation.BindView;
import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.listener.CustomOnClickListener;
import com.nfl.libraryoflibrary.utils.DialogTool;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.SoftKeyBoardTool;
import com.nfl.libraryoflibrary.utils.ToastTool;
import com.nfl.libraryoflibrary.utils.Voice2CharacterTool;
import com.nfl.libraryoflibrary.utils.net.CustomHttpHelper;
import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

/**
 * Created by fuli.niu on 2017/2/23.
 * 继承自该类的 activity 不需要处理 ActionBar
 */

public abstract class CommonActionBarActivity extends FragmentActivity implements CommonActionBarActivityPretreatments {

    protected Context context;
    @BindView
    private ImageView iv_back;
    private TextView tv_title;
    // @BindView(R.id.iv_alternate)
    protected ImageView iv_alternate;

    private TextView title_conference_search_button;
    protected LinearLayout ll_pad_container;
    protected LinearLayout ll_data_binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }

    @Override
    protected void onPause() {
        CustomHttpHelper.cancelAll();
        CustomProgressBarDialog.dimissProgressBarDialog();
        super.onPause();
    }

    /**
     * 覆写为了 setContentView 的使用习惯不变
     *
     * @param layoutResID
     */
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_common);
        initActionBarAndViewStub();
        View view = LayoutInflater.from(CommonActionBarActivity.this).inflate(layoutResID , ll_data_binding , false) ;
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

    /**
     * call after {@link #setContentView(int)} or {@link #setContentView(View)}
     *
     * @param viewGroup
     */
    public void openVoice2Charactor(final ViewGroup viewGroup) {
        iv_alternate.setVisibility(View.VISIBLE);
        iv_alternate.setOnClickListener(new CustomOnClickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                final View focusView = viewGroup.findFocus();
                if (null == focusView) {
                    DialogTool.displayAlertDialogOneButton(CommonActionBarActivity.this, "请先点击要输入的字段");
                } else if (focusView instanceof EditText) {
                    LogTool.i("焦点 View ：" + focusView);
                    // 启动讯飞语音识别
                    Voice2CharacterTool voice2CharacterTool = new Voice2CharacterTool(CommonActionBarActivity.this);
                    voice2CharacterTool.setVoice2CharacterListener(
                            new Voice2CharacterTool.Voice2CharacterListener() {
                                @Override
                                public void onEndOfSpeechWithoutPunctuation(String resultString) {
                                    super.onEndOfSpeech(resultString);
                                    EditText etFocusView = (EditText) focusView;
                                    String etString = etFocusView.getText().toString();
                                    if (!TextUtils.isEmpty(resultString)) {
                                        if (TextUtils.isEmpty(etString)) {
                                            etFocusView.setText(resultString);
                                        } else {
                                            etFocusView.setText(etString + resultString);
                                        }
                                        // 移动光标到末尾
                                        etFocusView.setSelection(etFocusView.getText().length());
                                    } else {
                                        // 语音内容为空时不需要处理
                                    }
                                    ToastTool.showLongToast("识别结果：" + resultString);
                                }

                                @Override
                                public void onFailure() {
                                    super.onFailure();
                                    ToastTool.showLongToast("识别不成功");
                                }
                            }
                    );
                    voice2CharacterTool.start();
                } else {
                    LogTool.i("没有获得焦点 View");
                }
            }
        });
    }

    private void initActionBarAndViewStub() {
        iv_alternate = (ImageView) findViewById(R.id.iv_alternate);
        title_conference_search_button = (TextView) findViewById(R.id.title_conference_search_button);
        ll_pad_container = (LinearLayout) findViewById(R.id.ll_pad_container);
        ll_data_binding = (LinearLayout) findViewById(R.id.ll_data_binding);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new CustomOnClickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                SoftKeyBoardTool.hideSoftKeyBoard(CommonActionBarActivity.this);
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

    public String getActionBarTitle() {
        if (null == tv_title) {
            return "";
        }
        return tv_title.getText().toString();
    }

    protected void setMenuText(String str, CustomOnClickListener onClickListener) {
        title_conference_search_button.setVisibility(View.VISIBLE);
        title_conference_search_button.setText(str);
        title_conference_search_button.setOnClickListener(onClickListener);
    }

    protected void setMenuText(int strId, CustomOnClickListener onClickListener) {
        this.setMenuText(getResources().getString(strId), onClickListener);
    }

    protected void hiddenBackIcon() {
        iv_back.setVisibility(View.GONE);
    }
}
