package com.nfl.libraryoflibrary.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.listener.CustomOnClickListener;

/**
 * Created by fuli.niu on 2016/12/19.
 */

public class CustomSearchBar extends RelativeLayout {

    private Context context;
    private RelativeLayout relativeLayout;
    private EditText et_search;
    private ImageView iv_search_delete;
    private CustomTextWatcher customTextWatcher;
    private OnCleanListener onCleanListener;
    private OnSearchButtonClickListener onSearchButtonClickListener;

    public CustomSearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        relativeLayout = (RelativeLayout)
                LayoutInflater.from(context).inflate(R.layout.custom_edit_text, null);
        this.addView(relativeLayout);
        initView();
    }

    public CustomSearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSearchBar(Context context) {
        this(context, null, 0);
    }


    private void initView() {
        et_search = (EditText) relativeLayout.findViewById(R.id.et_search);
        et_search.addTextChangedListener(textWatcher);
        et_search.setOnEditorActionListener(onEditorActionListener);
        iv_search_delete = (ImageView) findViewById(R.id.iv_search_delete);
        iv_search_delete.setOnClickListener(onClickListener);
    }

    private CustomOnClickListener onClickListener = new CustomOnClickListener() {
        @Override
        public void onClick(View v) {
            super.onClick(v);
            int viewId = v.getId();
            if (viewId == R.id.iv_search_delete) {
                et_search.setText(null);
                if (null != onCleanListener) {
                    onCleanListener.onClick();
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (null != customTextWatcher) {
                customTextWatcher.beforeTextChanged(s, start, count, after);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            iv_search_delete.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            if (null != customTextWatcher) {
                customTextWatcher.onTextChanged(s, start, before, count);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null != customTextWatcher) {
                customTextWatcher.afterTextChanged(s);
            }
        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //当actionId == XX_SEND 或者 XX_DONE时都触发
            //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
            //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                //处理事件
                if (null != onSearchButtonClickListener) {
                    onSearchButtonClickListener.onSearchButtonClick();
                }
            }
            return false;
        }
    };

    public void setHint(String str) {
        et_search.setHint(str);
    }

    public void setHint(int resid) {
        et_search.setHint(resid);
    }

    public void clearSearchContent() {
        et_search.setText("");
    }

    /**
     * @return 搜索框内容
     */
    public String getContent(){
        return et_search.getText().toString() ;
    }

    /**
     * 设置自定义TextWatcher
     *
     * @param customTextWatcher
     */
    public void setCustomTextWatcher(CustomTextWatcher customTextWatcher) {
        this.customTextWatcher = customTextWatcher;
    }

    /**
     *  自定义TextWatcher
     */
    public interface CustomTextWatcher {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }

    public interface OnCleanListener {
        void onClick();
    }

    public interface OnSearchButtonClickListener {
        void onSearchButtonClick();
    }

    public void setOnCleanListener(OnCleanListener onCleanListener) {
        this.onCleanListener = onCleanListener;
    }

    public void setOnSearchButtonClickListener(OnSearchButtonClickListener onSearchButtonClickListener) {
        this.onSearchButtonClickListener = onSearchButtonClickListener;
    }
}
